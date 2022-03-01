package net.wangyl.goldenlife.ui.slideshow

import androidx.paging.PagingSource
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.databinding.ItemTextViewBinding
import net.wangyl.goldenlife.extension.getK
import net.wangyl.goldenlife.model.PostData
import net.wangyl.goldenlife.mvi.base.BaseListFragment
import net.wangyl.goldenlife.mvi.base.MyBaseViewHolder
import net.wangyl.goldenlife.mvi.base.PageInfo

class SlideshowFragment : BaseListFragment<PostData>() {

    val repo: Repository = getK()

    override suspend fun loader(params: PageInfo): Status<List<PostData>> {
        return repo.getPosts()
    }

    override fun bindItem(holder: MyBaseViewHolder, item: PostData, payloads: List<Any>?) {
        (holder.dataBinding as ItemTextViewBinding).tv.text = item.getItemContent()
    }



}