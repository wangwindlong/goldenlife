package net.wangyl.life.api

import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.http.mapper.ApiErrorMapper
import timber.log.Timber

object ErrorResponseMapper : ApiErrorMapper<ErrorMessage> {
    override fun map(apiErrorResponse: ApiResponse.ApiError<*>): ErrorMessage {
        Timber.d("ErrorResponseMapper map apiErrorResponse code=${apiErrorResponse.code} apiErrorResponse=$apiErrorResponse")
        return ErrorMessage(apiErrorResponse).apply {
            Timber.d("ErrorResponseMapper result=$this code=${this.code}")
        }
    }
}