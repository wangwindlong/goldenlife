package net.wangyl.goldenlife.mvi.base


interface Event

data class DetailEvent<T>(val value: T, var extra: String? = "") : Event {

}

