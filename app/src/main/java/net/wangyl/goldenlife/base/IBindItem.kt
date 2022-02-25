package net.wangyl.goldenlife.base

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.wangyl.goldenlife.model.BaseModel

interface IBindItem<Data: BaseModel, VH:BaseViewHolder> {
    fun bindItem(holder: VH, item: Data, payloads: List<Any>? = null)
}