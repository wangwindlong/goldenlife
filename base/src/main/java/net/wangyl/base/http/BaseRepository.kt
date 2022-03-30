package net.wangyl.base.http

import kotlinx.coroutines.*
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.data.handleException
import net.wangyl.base.data.successOr
import net.wangyl.base.util.Cache
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds


const val API_DELAY = 1000L //api调用延时时间 单位 ms
const val API_EXPIRE = 60*30L //api缓存时间 单位 s
const val API_MAX = 30 //最多缓存api个数

/**
 * apiDelay
 */
open class BaseRepository(maxSize: Int = API_MAX, val apiDelay: Long = API_DELAY,
                          apiExpire: Long = API_EXPIRE) {
    val cache = Cache<RequestCacheKey, Any>(
        maxSize = maxSize,
        entryLifetime = apiExpire.seconds
    )

    companion object {
        data class RequestCacheKey(
            val method: String,
            val queryItems: Map<String, String>,
        )

        @JvmStatic
        fun buildKey(method: String, queryItems: Map<String, Any?>): RequestCacheKey {
            return RequestCacheKey(
                method = method,
                queryItems = queryItems.entries
                    .mapNotNull { (k, v) ->
                        if (v == null) null
                        else k to v.toString()
                    }
                    .toMap()
            )
        }

        @JvmStatic
        fun getCache() {

        }

        @JvmStatic
        fun saveCache() {

        }
    }
}

suspend inline fun <reified T> BaseRepository.apiCall(
    method: String? = null, params: Map<String, Any?> = emptyMap(),
    crossinline request: suspend BaseRepository.() -> ApiResponse<T>,
): ApiResponse<T> {
    var cacheKey: BaseRepository.Companion.RequestCacheKey? = null
    if (method != null) {
        cacheKey = BaseRepository.buildKey(method, params)
        val data = cache[cacheKey]
        if (data is T) {
            return ApiResponse.ApiSuccess(data, true)
        }
    }
    val data = coroutineScope {
        delay(apiDelay)
        this.runCatching {
            request()
        }.onSuccess {
            Timber.d("apiCall onSuccess result= ${it.data}")
            //status code 为200，继续判断 errorCode 是否为 0
            when (it.success) {
                true -> ApiResponse.ApiSuccess(data = it.data, true)
                false -> ApiResponse.ApiError(handleException(ErrorMessage(it)))
            }
        }.onFailure { throwable ->
            Timber.e("apiCall onFailure throwable= $throwable")
            ApiResponse.ApiError<T>(handleException(throwable))
        }
    }
    return data.getOrElse { ApiResponse.ApiError(handleException(it)) }
}


