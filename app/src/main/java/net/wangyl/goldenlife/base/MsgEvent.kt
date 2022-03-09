package net.wangyl.goldenlife.base

import java.util.*

data class MsgEvent<T>(val from: String, val msg: T? = null, val other: Any? = null) {

}