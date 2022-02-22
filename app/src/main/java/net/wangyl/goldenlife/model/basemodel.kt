package net.wangyl.goldenlife.model

interface BaseListModel<Key: Any, T> {
    val displayList: List<T>?
    val nextKey: Key?
}

interface ListModel2<T>: BaseListModel<String, T> {}
interface ListModel<T>: BaseListModel<Int, T> {}