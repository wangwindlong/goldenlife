package net.wangyl.base.http.mapper

import net.wangyl.base.data.ApiResponse


interface ApiErrorMapper<V> {
    fun map(apiErrorResponse: ApiResponse.ApiError<*>): V
}