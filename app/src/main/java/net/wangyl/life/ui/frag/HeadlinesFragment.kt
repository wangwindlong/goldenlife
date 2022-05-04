package net.wangyl.life.ui.frag

import androidx.paging.PagingSource
import net.wangyl.base.base.BasePagingFragment
import net.wangyl.base.adapter.BasePagedAdapter
import net.wangyl.base.databinding.BaseItemTextViewBinding
import net.wangyl.life.api.Repository
import net.wangyl.life.model.Article
import net.wangyl.life.obj.Global
import org.koin.android.ext.android.inject
import timber.log.Timber

class HeadlinesFragment : BasePagingFragment<Int, Article, BaseItemTextViewBinding>() {

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
        position: Int?,
        params: PagingSource.LoadParams<Int>
    ): List<Article>? {
        val map = HashMap<String, String>()
        map["op"] = "getHeadlines"
        map["sid"] = Global.sessionId ?: "tktb7mgjavsn1f1topki9tka5j" //

        map["feed_id"] = "-1" //-1

        map["show_excerpt"] = "true"
        map["excerpt_length"] = "256" //256

        map["show_content"] = "true"
        map["include_attachments"] = "true"
        map["view_mode"] = "adaptive" //adaptive

        map["limit"] = "15" //15

        map["offset"] = 0.toString()
        map["skip"] = "${position ?: 0}" //0

        map["include_nested"] = "true"
        map["has_sandbox"] = "true"
        map["order_by"] = "default" //default

        val res = repo.apiService.headlines(map).data
        Timber.d("fetchData headlines result=${res?.size}")
        return res
    }

    override fun bindData(
        holder: BasePagedAdapter.BaseHolder<BaseItemTextViewBinding>,
        model: Article?,
        position: Int
    ) {
        holder.binding.tv.text = model?.getItemContent()
    }


    override fun nextKey(position: Int?, dataList: List<Article>): Int? {
        Timber.d("nextKey position= $position dataList=${dataList.size}")
        if (dataList.isEmpty()) return null
        return (position ?: 0) + 1
    }
}