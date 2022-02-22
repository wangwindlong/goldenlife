package net.wangyl.goldenlife.mvi


interface Event

data class BaseEvent<T>(val value: T):Event {

}

