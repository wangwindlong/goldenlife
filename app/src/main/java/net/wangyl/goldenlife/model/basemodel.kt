package net.wangyl.goldenlife.model

import android.os.Parcelable
import net.wangyl.goldenlife.R
import java.io.Serializable

interface BaseListModel<Key : Any, T> {
    val displayList: List<T>?
    val nextKey: Key?
}

interface BaseModel : Parcelable, Serializable {
    fun getItemId(): String = ""
    fun getItemContent(): String = ""
}

//recyclerview 多布局时需要使用到
interface BaseItem : BaseModel {
    fun getItemType(): Int = R.layout.item_text_view
}

interface ListModel2<T> : BaseListModel<String, T> {}
interface ListModel<T> : BaseListModel<Int, T> {}