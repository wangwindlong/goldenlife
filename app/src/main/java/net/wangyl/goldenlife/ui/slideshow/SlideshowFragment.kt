package net.wangyl.goldenlife.ui.slideshow

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.paging.PagingSource
import net.wangyl.base.BasePagingFragment
import net.wangyl.base.adapter.BasePagedAdapter
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.base.data.Status
import net.wangyl.base.databinding.BaseItemTextViewBinding
import net.wangyl.base.databinding.BasePagingBinding
import net.wangyl.base.extension.navTo
import net.wangyl.goldenlife.model.PostData
import net.wangyl.base.mvi.orbit.*
import net.wangyl.goldenlife.ui.settings.SettingsFragment
import org.koin.android.ext.android.inject
import timber.log.Timber

class SlideshowFragment : BasePagingFragment<PostData, BaseItemTextViewBinding>() {

    val repo: Repository by inject()

//    override suspend fun loader(params: PageInfo): Status<List<PostData>> {
//        return repo.getPosts()
//    }
//
//    override fun bindItem(holder: MyBaseViewHolder, item: PostData, payloads: List<Any>?) {
//        (holder.dataBinding as BaseItemTextViewBinding).tv.text = item.getItemContent()
//    }
//
//    override fun initData(savedInstanceState: Bundle?) {
//        super.initData(savedInstanceState)
//        Timber.d("initData args=${savedInstanceState?.get("args_1")}")
//    }
//
//    override fun initView(v: View?, savedInstanceState: Bundle?) {
//        super.initView(v, savedInstanceState)
//    }
//
//    override fun sideEffect(event: Event) {
//        when (event) {
//            is DetailEvent<*> -> {
//                Timber.d("navigateTo event ${event.value}")
//                navTo(
//                    R.id.nav_settings, bundleOf(
//                        "itemId" to (event.value as PostData).getItemId(),
//                        "item" to event.value
//                    )//.apply { putParcelable("item", event.value)  }
//                ) {
//                    goActivity(SettingsFragment::class.java.name)
//                }
////                findNavController().navigate(
////                    SettingsFragmentDi.actionListFragmentToDetailFragment(
////                        sideEffect.post
////                    )
////                )
//            }
//        }
//    }

    override suspend fun fetchData(
        position: Int,
        params: PagingSource.LoadParams<Int>
    ): List<PostData> {
        return repo.getPosts()
    }

    override fun bindData(
        holder: BasePagedAdapter.BaseHolder<BaseItemTextViewBinding>,
        model: PostData?,
        position: Int
    ) {
        holder.binding.tv.text = model?.getItemContent()
    }
}