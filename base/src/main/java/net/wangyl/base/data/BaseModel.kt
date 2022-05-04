package net.wangyl.base.data

import android.os.Parcelable
import net.wangyl.base.mvi.orbit.defaultItem
import java.io.Serializable

interface BaseListModel<Key, T> {
    val dataList: List<T>?
    val nextKey: Key?
}

interface BaseModel : Parcelable, Serializable {
//    val success: Boolean
    fun getItemId(): String = ""
    fun getItemContent(): String = ""
}

//recyclerview 多布局时需要使用到
interface BaseItem : BaseModel {
    fun getItemType(): Int = defaultItem
}

interface BaseEntity {
    val localid: Long
}

interface IdEntity : BaseEntity {
    val id: Long
}

interface PaginatedEntry : IdEntity {
    val page: Int
}

interface ListModel2<T> : BaseListModel<String, T> {}
interface ListModel<Data> : BaseListModel<Int, Data> {}