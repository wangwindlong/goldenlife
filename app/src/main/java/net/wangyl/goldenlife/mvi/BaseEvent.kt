package net.wangyl.goldenlife.mvi


interface Event

data class DetailEvent<T>(val value: T) : Event {

}

