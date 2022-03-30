package net.wangyl.goldenlife.api

import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.http.mapper.ApiErrorMapper

object ErrorResponseMapper : ApiErrorMapper<ErrorMessage> {
    override fun map(apiErrorResponse: ApiResponse.ApiError<*>): ErrorMessage {
        return ErrorMessage(apiErrorResponse)
    }
}