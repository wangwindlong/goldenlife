package net.wangyl.base.http.retro

import net.wangyl.base.data.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import timber.log.Timber
import java.lang.reflect.Type

/**
 *
 * S  是接口请求成功时返回的类
 * E 是其他类型，如错误数据格式解析，适用于请求正确和错误返回不同数据格式的情况
 */
class NetworkResponseAdapter(
    private val successType: Type,
    private val errorConverter: Converter<ResponseBody, Type>? = null
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return NetworkResponseCall(call, errorConverter)
    }
}


//sealed class NetworkResponse<out T : Any, out U : Any> {
//    /**
//     * Success response with body
//     */
//    data class Success<T : Any>(val body: T) : NetworkResponse<T, Nothing>()
//
//    /**
//     * Failure response with body
//     */
//    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()
//
//    /**
//     * Network error
//     */
//    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()
//
//    /**
//     * For example, json parsing error
//     */
//    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()
//}
