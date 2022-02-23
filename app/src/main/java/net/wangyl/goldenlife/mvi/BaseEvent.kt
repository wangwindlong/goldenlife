package net.wangyl.goldenlife.mvi


interface Event

data class DertailEvent<T>(val value: T):Event {

}

