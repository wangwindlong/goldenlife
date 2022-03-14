package net.wangyl.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import net.wangyl.base.databinding.BaseFailureListItemBinding

class ErrorLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<StateViewHolder>() {
    override fun onBindViewHolder(holder: StateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): StateViewHolder {
        return StateViewHolder.create(parent, retry)
    }

}

class StateViewHolder(
    private val binding: BaseFailureListItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.retry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progress.isVisible = loadState is LoadState.Loading
        binding.errorGroup.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): StateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = BaseFailureListItemBinding.inflate(layoutInflater, parent, false)
            return StateViewHolder(binding, retry)
        }
    }
}