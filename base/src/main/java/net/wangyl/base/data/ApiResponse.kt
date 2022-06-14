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
import net.wangyl.base.util.isSameWrapClass
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import kotlin.coroutines.cancellation.CancellationException


sealed class ApiResponse<T> : BaseResponse<T>() {

    data class ApiSuccess<T>(
        override val data: T, override val success: Boolean = true,
        override val msg: String = "", override val status: String = "",
    ) : ApiResponse<T>() {
//        override val nextKey: Int?
//            get() = TODO("Not yet implemented")
    }

    //服务端报错 502
    data class ApiError<T>(val exception: Exception) : ApiResponse<T>()

    //条件错误，返回服务端定义的错误码
    data class CondError<T>(
        override val data: T?, override val success: Boolean = false,
        override val msg: String? = "", override val status: String = ""
    ) : ApiResponse<T>()

    data class NetworkError<T>(val error: Throwable?) : ApiResponse<T>()

    companion object {

        @JvmStatic
        fun <T, E> of(
            targetType: Type,
            response: Response<T>,
            errorConverter: retrofit2.Converter<ResponseBody, E>? = null
        ): ApiResponse<T> = try {
            val code = response.code()
            val error = response.errorBody()
            val e = when {
                error == null -> null
                error.contentLength() == 0L -> null
                else -> try {
                    errorConverter?.convert(error)
                } catch (ex: Exception) {
                    null
                }
            }
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    if (data is Converter<*> && !isSameWrapClass(data.javaClass, targetType)) {
                        data.convert()
                    } else {
                        ApiSuccess(data)
                    }
                } else {
                    CondError(data, false,  "数据异常", "-1")
                }
            } else {
                NetworkError(ErrorMessage(code = code, error = "服务器错误: $e"))
            } as ApiResponse<T>
        } catch (ex: Exception) {
            ApiError(ex)
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
    Timber.d("ApiResponse onSuccess this=${this}")
    if (this is ApiResponse.ApiSuccess) {
        onResult(this.data)
    }
    return this
}
//用于需要处理服务端提示的错误信息
@JvmSynthetic
suspend inline fun <T> ApiResponse<T>.onApiError(
    crossinline onResult: suspend ApiResponse.CondError<T>.(T?) -> Unit
): ApiResponse<T> {
    Timber.d("onApiError onSuccess this=${this}")
    if (this is ApiResponse.CondError) {
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
        code = response?.parseCode(),
        errorMsg = response?.parseMsg(),
        error = response?.parseMsg()
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

fun <T> BaseResponse<T>.parseCode(): Int {
    return when (this) {
        is ApiResponse.ApiError<*> -> if (exception is ErrorMessage) exception.code ?: -1 else -1
        is ApiResponse.CondError<*> -> this.code
        else -> code

    }
}

fun <T> BaseResponse<T>.parseMsg(): String? {
    return when (this) {
        is ApiResponse.ApiError<*> -> if (exception is ErrorMessage) exception.error else exception.toString()
        is ApiResponse.CondError<*> -> this.msg
        else -> msg

    }
}

//typealias ErrorHandler = (Throwable) -> ErrorMessage?
fun handleException(throwable: Throwable, handler: HttpErrorHandler? = null) = when (throwable) {
    is UnknownHostException -> ErrorMessage(HttpError.NETWORK_ERROR, throwable.message)
    is HttpException -> {
//        val errorModel = throwable.response()?.errorBody()?.string()?.run {
//            Gson().fromJson(this, ErrorMessage::class.java)
//        } ?: ErrorMessage()
        ErrorMessage(
            code = throwable.code(),
            errorMsg = throwable.message,
            error = throwable.message
        )
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
