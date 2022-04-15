package net.wangyl.base.annotation

import kotlin.reflect.KClass

annotation class WrapWith(val wrapClass: KClass<out Any>)

annotation class ErrorWith(val errClass: KClass<out Any>, val isWrap: Boolean = false)
