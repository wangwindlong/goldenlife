package net.wangyl.base.interf

import net.wangyl.base.data.ErrorMessage

interface HttpErrorHandler {
    fun handle(throwable: Throwable): ErrorMessage?

//    operator fun invoke() {
//
//    }
}