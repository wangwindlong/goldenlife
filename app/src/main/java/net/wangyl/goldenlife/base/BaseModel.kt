package net.wangyl.goldenlife.base

import android.os.Parcelable
import net.wangyl.goldenlife.mvi.base.defaultItem
import java.io.Serializable

interface BaseListModel<Key : Any, T> {
    val displayList: T?
    val nextKey: Key?
}

interface BaseModel : Parcelable, Serializable {
    fun getItemId(): String = ""
    fun getItemContent(): String = ""
}

//recyclerview 多布局时需要使用到
interface BaseItem : BaseModel {
    fun getItemType(): Int = defaultItem
}

interface ListModel2<T> : BaseListModel<String, T> {}
interface ListModel<T> : BaseListModel<Int, T> {}