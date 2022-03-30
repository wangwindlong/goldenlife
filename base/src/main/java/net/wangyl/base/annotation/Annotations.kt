package net.wangyl.base.annotation

import net.wangyl.base.interf.Converter
import kotlin.reflect.KClass

annotation class WrapWith(val wrapClass: KClass<out Any>)
