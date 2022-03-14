package net.wangyl.base.mvi.orbit


interface Event

data class DetailEvent<T>(val value: T, var extra: String? = "") : Event {

}

