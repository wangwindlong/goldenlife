package net.wangyl.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import net.wangyl.base.data.BaseItem
import net.wangyl.base.mvi.orbit.defaultItem

open class BasePagedAdapter<T : Any, I : ViewDataBinding> constructor(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {
    private lateinit var context: Context
    private val mMultiType: MultiType<T> = MultiType()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position)
        bindItem(holder as BaseHolder<I>, model, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding: I = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return BaseHolder(binding)
    }

    open fun bindItem(holder: BaseHolder<I>, model: T?, position: Int) {

    }

    //返回position 后面通过data 绑定的layoutid加载布局，需要每个data绑定布局，有简便方法？
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is BaseItem) item.getItemType() else defaultItem
    }

    class BaseHolder<I : ViewDataBinding>(val binding: I) : RecyclerView.ViewHolder(binding.root)
}