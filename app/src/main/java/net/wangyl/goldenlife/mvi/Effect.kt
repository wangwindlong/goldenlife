package net.wangyl.goldenlife.mvi

sealed class ListSideEffect<out T> {
    data class NavigateToDetail<out T>(val item: T) : ListSideEffect<T>()
}