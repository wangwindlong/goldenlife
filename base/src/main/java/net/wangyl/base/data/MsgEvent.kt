package net.wangyl.base.data

data class MsgEvent<T>(val from: String, val msg: T? = null, val other: Any? = null) {

}