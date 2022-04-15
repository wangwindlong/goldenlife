package net.wangyl.base.http

import kotlinx.coroutines.*
import net.wangyl.base.Configs
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.data.handleException
import net.wangyl.base.data.successOr
import net.wangyl.base.util.Cache
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds




/**
 * apiDelay
 */
interface BaseRepository {


    companion object {
    }
}

suspend inline fun <reified T> BaseRepository.Companion.apiCall(
    crossinline request: suspend () -> ApiResponse<T>,
): ApiResponse<T> {

    return coroutineScope {
        delay(Configs.API_DELAY)
        this.runCatching {
            request()
        }.onSuccess {
//            when (it.success) {
//                true -> ApiResponse.ApiSuccess(data = it.data, true)
//                false -> ApiResponse.ApiError(handleException(ErrorMessage(it)))
//            }
        }.onFailure { throwable ->
            Timber.e("apiCall onFailure throwable= $throwable")
            ApiResponse.ApiError<T>(handleException(throwable))
        }
    }.getOrElse { ApiResponse.ApiError(handleException(it)) }
}


