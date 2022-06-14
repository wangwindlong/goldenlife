package net.wangyl.life.api

import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.ErrorMessage
import net.wangyl.life.model.NETWORK_ERROR
import net.wangyl.life.model.SERVER_ERROR
import timber.log.Timber

//统一处理接口返回值，直接扔出异常或者 其他
@JvmSynthetic
fun <T> ApiResponse<T>.getOrThrow(): T {
    Timber.d("ApiResponse onSuccess this=${this}")
    return when(this) {
        is ApiResponse.ApiSuccess -> data
        is ApiResponse.CondError -> throw ErrorMessage(code = code, error = msg, errorMsg = msg)
        is ApiResponse.NetworkError -> throw Exception(NETWORK_ERROR)
        else -> throw Exception(SERVER_ERROR)
    }
}