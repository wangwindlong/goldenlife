package net.wangyl.base.http.retro

import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.ErrorMessage
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import timber.log.Timber
import java.io.IOException

/**
 *  R 是RSSData<Foo>包裹类型或者Foo数据类型，根据addCallAdapterFactory注册时是否传包裹类来定
 *  E 是其他类型，如错误数据格式解析，适用于请求正确和错误返回不同数据格式的情况
 */
internal class NetworkResponseCall<T : Any, E: Any>(
    private val delegate: Call<T>,
    private val errorConverter: Converter<ResponseBody, E>? = null
) : Call<ApiResponse<T>> {

    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        return delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
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
                val apiResponse = if (response.isSuccessful) {
                    Timber.d("apicall success response.body()=${body}")
                    if (body != null) {
                        if (body is net.wangyl.base.interf.Converter<*>) { //如果是自定义包裹类型，转换之
                            body.convert()
                        } else {
                            ApiResponse.ApiSuccess(body)
                        }
                    } else {
                        ApiResponse.NetworkError(ErrorMessage(code = code, error = "服务器错误: $e"))
                    }
                } else {
                    ApiResponse.NetworkError(ErrorMessage(code = code, error = "服务器错误: $e"))
                }
//                val apiResponse = ApiResponse.of({ response }, errorData)
                Timber.d("onResponse apiResponse=$apiResponse")
                callback.onResponse(this@NetworkResponseCall, Response.success(apiResponse as ApiResponse<T>) )
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val networkResponse = when (throwable) {
                    is IOException -> ApiResponse.NetworkError<T>(throwable)
                    else -> ApiResponse.NetworkError(throwable)
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(networkResponse as ApiResponse<T>))
            }
        })
    }

    override fun isExecuted() = delegate.isExecuted

    override fun clone() = NetworkResponseCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<ApiResponse<T>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support execute")
    }

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}