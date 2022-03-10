package net.wangyl.goldenlife.mvi.orbit

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.wangyl.goldenlife.base.BaseModel

interface IBindItem<Data : BaseModel, VH : BaseViewHolder> {
    fun bindItem(holder: VH, item: Data, payloads: List<Any>? = null)
}