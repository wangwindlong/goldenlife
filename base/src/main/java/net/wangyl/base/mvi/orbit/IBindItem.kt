package net.wangyl.base.mvi.orbit

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.wangyl.base.data.BaseModel

interface IBindItem<Data : BaseModel, VH : BaseViewHolder> {
    fun bindItem(holder: VH, item: Data, payloads: List<Any>? = null)
}