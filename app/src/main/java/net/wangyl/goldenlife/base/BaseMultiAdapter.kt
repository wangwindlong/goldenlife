package net.wangyl.goldenlife.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import net.wangyl.goldenlife.model.BaseModel
import net.wangyl.goldenlife.ui.MultiTypeDelegate
import net.wangyl.goldenlife.ui.MyBaseViewHolder
import java.lang.NullPointerException

class BaseMultiAdapter<Data : BaseModel>(layouts: List<Int>, val binder: IBindItem<Data, MyBaseViewHolder>):
    BaseDelegateMultiAdapter<Data, MyBaseViewHolder>(), LoadMoreModule {

    init {
        if (layouts.isEmpty()) throw NullPointerException("fun getItemLayouts() : List<Int> must return more than one items")
        setMultiTypeDelegate(MultiTypeDelegate(layouts))
        setDiffCallback(object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.getItemId() == newItem.getItemId()
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return (oldItem.getItemContent() == newItem.getItemContent()
                        && oldItem.equals(newItem))
            }
        })
    }

    override fun convert(holder: MyBaseViewHolder, item: Data) {
        binder.bindItem(holder, item)
    }

    override fun convert(holder: MyBaseViewHolder, item: Data, payloads: List<Any>) {
        binder.bindItem(holder, item, payloads)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): MyBaseViewHolder {
        val delgate = getMultiTypeDelegate()
        val itemId = delgate?.getLayoutId(viewType)
            ?: throw NullPointerException("未找到 viewType=$viewType 的布局")
        val itemBinding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), itemId, parent, false)
        return MyBaseViewHolder(itemBinding.root).apply { dataBinding = itemBinding }
    }
}