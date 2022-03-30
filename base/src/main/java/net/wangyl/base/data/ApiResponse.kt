/*
 * Copyright 2021 Mikołaj Leszczyński & Appmattus Limited
 * Copyright 2020 Babylon Partners Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * File modified by Mikołaj Leszczyński & Appmattus Limited
 * See: https://github.com/orbit-mvi/orbit-mvi/compare/c5b8b3f2b83b5972ba2ad98f73f75086a89653d3...main
 */

package net.wangyl.base.data

import net.wangyl.base.enums.HttpError
import net.wangyl.base.http.mapper.ApiErrorMapper
import net.wangyl.base.interf.Converter
import net.wangyl.base.interf.HttpErrorHandler
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import kotlin.coroutines.cancellation.CancellationException


sealed class ApiResponse<T>: BaseResponse<T>() {

    data class ApiSuccess<T>(override val data : T, override val success : Boolean = true,
            override val msg : String = "",override val status : String = "", ) : ApiResponse<T>() {
//        override val nextKey: Int?
//            get() = TODO("Not yet implemented")
    }

    //Failure response with body
    data class ApiError<T>(val exception: Exception) : ApiResponse<T>() {
    }

    data class NetworkError<T>(val error: Throwable?) : ApiResponse<T>()

    companion object {
        @JvmStatic
        inline fun <T, R> of(crossinline f: () -> Response<T>, e: R? = null): ApiResponse<*> = try {
            val response = f()
            val code = response.code()
            if (response.isSuccessful) {
                val data = response.body()
                Timber.d("response.body()=${data}")
                if (data != null) {
                    if (data is Converter<*>) {
                        data.convert()
                    } else {
                        ApiSuccess(data)
                    }
                } else {
                    NetworkError(ErrorMessage(code = code, error = "服务器错误: $e"))
                }
            } else {
                NetworkError(ErrorMessage(code = code, error = "服务器错误: $e"))
            }
        } catch (ex: Exception) {
            ApiError<Nothing>(ex)
        }
    }
}

//用于不需要回调的情形，直接获取返回的数据
@JvmSynthetic
inline fun <T> ApiResponse<T>.successOr(fallback: () -> T): T {
    return (this as? ApiResponse.ApiSuccess<T>)?.data ?: fallback()
}

//用于需要处理回调，如存入本地数据库，合并数据等
@JvmSynthetic
suspend inline fun <T> ApiResponse<T>.onSuccess(
    crossinline onResult: suspend ApiResponse.ApiSuccess<T>.(T) -> Unit
): ApiResponse<T> {
    if (this is ApiResponse.ApiSuccess) {
        onResult(this.data)
    }
    return this
}

@JvmSynthetic
inline fun <T, V> ApiResponse<T>.onError(
    mapper: ApiErrorMapper<V>,
    crossinline onResult: V.() -> Unit
): ApiResponse<T> {
    if (this is ApiResponse.ApiError) {
        onResult(mapper.map(this))
    }
    return this
}



data class ErrorMessage(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val code: Int? = 0, val errorMsg: String? = "",
    val error: String? = null
) : Exception(error) {

    constructor(response: BaseResponse<*>?) : this(
        code = response?.code,
        errorMsg = response?.msg,
        error = response?.msg
    )

    constructor(httpError: HttpError, error: String?) : this(
        code = httpError.code,
        errorMsg = httpError.message,
        error = error
    )

    constructor(throwable: Throwable?, msg: String?) : this(
        code = -1,
        errorMsg = msg,
        error = msg
    )

}

open class BaseResponse<out T> {
    open val data: T? = null
    open val success: Boolean = true
    open val status: String = "" //成功与否的状态码，可以是字符串 可以是数字
    open val msg: String? = null
    open val code: Int = 0 //http返回码
    open val other: String = ""

    override fun toString(): String {
        return "BaseResponse(data=$data, success=$success, status='$status', msg=$msg, code=$code, other='$other')"
    }

}

//typealias ErrorHandler = (Throwable) -> ErrorMessage?
fun handleException(throwable: Throwable, handler: HttpErrorHandler? = null) = when (throwable) {
    is UnknownHostException -> ErrorMessage(HttpError.NETWORK_ERROR, throwable.message)
    is HttpException -> {
//        val errorModel = throwable.response()?.errorBody()?.string()?.run {
//            Gson().fromJson(this, ErrorMessage::class.java)
//        } ?: ErrorMessage()
        ErrorMessage(code = throwable.code(), errorMsg = throwable.message, error = throwable.message)
    }
    is ConnectException -> ErrorMessage(HttpError.CONNECT_ERROR, throwable.message)
    is SocketTimeoutException -> ErrorMessage(HttpError.TIMEOUT_ERROR, throwable.message)
//    is JsonParseException -> ErrorMessage(HttpError.JSON_PARSE_ERROR, throwable.message)
    is ErrorMessage -> throwable
    is CancellationException,
    is NullPointerException,
    is ClassCastException -> {
        ErrorMessage(errorMsg = "程序出错", error = throwable.message)
    }
    else -> handler?.handle(throwable) ?: ErrorMessage(HttpError.UNKNOWN, throwable.message)
}
