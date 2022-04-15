package net.wangyl.base.base

import android.os.Bundle
import android.text.TextUtils
import android.util.SparseArray
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import net.wangyl.base.R
import net.wangyl.base.adapter.BasePagedAdapter
import net.wangyl.base.adapter.ErrorLoadStateAdapter
import net.wangyl.base.data.BaseModel
import net.wangyl.base.data.MsgEvent
import net.wangyl.base.extension.gone
import net.wangyl.base.extension.toast
import net.wangyl.base.extension.visible
import net.wangyl.base.paging.PageSource
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


interface FetchAction<Key : Any, Model> {
    suspend fun fetchData(position: Key?, params: PagingSource.LoadParams<Key>): List<Model>?
    fun nextKey(position: Key?, dataList: List<Model>): Key? {
        return when (position) {
            is Int -> (if (dataList.isNotEmpty()) position + 1 else null) as Key
            else -> position
        }
    }
    fun prevKey(position: Key?, dataList: List<Model>): Key? {
        return null
    }
}

/**
 * 通用paging fragment，固定layout为base_paging
 */
//abstract class PagingFragment<Model : BaseModel, ITEM : ViewDataBinding, Adapter : BasePagedAdapter<Model, ITEM>> :
//    BasePagingFragment<BasePagingBinding, Model>(), FetchAction<Int, Model>

/**
 * paging3 列表基类 BD为item的binding
 */
abstract class BasePagingFragment<K : Any, T : BaseModel, BD : ViewDataBinding>(startKey: K? = null) :
    BaseFragment(),
    FetchAction<K, T> {
    val diffCallback = object : DiffUtilItemCallback<T>() {}
    val mViewModel: PagingViewModel by activityViewModels()

    var initKey: K? = startKey
    var pageSize: Int = 20
    var recyclerView: RecyclerView? = null
    var errorGroup: View? = null
    var retryBtn: View? = null
    var progress: ProgressBar? = null
    var errorMsg: TextView? = null

    //    var actions: MutableList<String> = mutableListOf()
    private val MENUID_LOOKUP = SparseArray<Any>(5)

    val adapter by lazy {
        initAdapter().apply {
//            if (this is BaseQuickAdapter<*, *>) emptyView = loadingView
            addLoadStateListener { combinedLoadStates ->
                // Handle the initial load state
                when (val loadState = combinedLoadStates.source.refresh) {
                    is LoadState.NotLoading -> {
                        progress?.isVisible = false
                        errorGroup?.isVisible = false
                        smartRefresh?.isRefreshing = false
                    }
                    is LoadState.Loading -> {
                        progress?.isVisible = true
                        errorGroup?.isVisible = false
                    }
                    is LoadState.Error -> {
                        progress?.isVisible = false
                        errorGroup?.isVisible = true
                        errorMsg?.text = loadState.error.localizedMessage
                        smartRefresh?.isRefreshing = false
                    }
                }

                // Show message to the user when an error comes while loading the next page
                val errorState = combinedLoadStates.source.append as? LoadState.Error
                    ?: combinedLoadStates.append as? LoadState.Error
                errorState?.let {
                    toast(errorState.error.localizedMessage)
                }
            }
        }
    }


    open fun initAdapter(): BasePagedAdapter<T, BD> {
        return object : BasePagedAdapter<T, BD>(object : DiffUtilItemCallback<T>() {}) {
            override fun bindItem(holder: BaseHolder<BD>, model: T?, position: Int) {
                super.bindItem(holder, model, position)
                bindData(holder, model, position)
            }
        }
    }

    //绑定item布局
    open fun bindData(holder: BasePagedAdapter.BaseHolder<BD>, model: T?, position: Int) {

    }

    var smartRefresh: SwipeRefreshLayout? = null
        set(value) {
            field = value
            value?.isRefreshing = false
            value?.setOnRefreshListener {
                adapter.refresh()
            }
//            value?.setOnLoadMoreListener {
//                page++
//                loadMore()
//            }
        }

//    val loadingView by lazy {
//        LoadingTip(mActivity).apply { setOnClickListener { loadData() } }
//    }

    override fun getLayoutId(): Int = R.layout.base_paging

    override fun initView(v: View?, savedInstanceState: Bundle?) {
        super.initView(v, savedInstanceState)
        smartRefresh = v!!.findViewById(R.id.smartRefresh)
        mToolbar = v.findViewById(R.id.base_toolbar)
        errorMsg = v.findViewById(R.id.error_msg)
        progress = v.findViewById(R.id.progress)
//        smartRefresh?.setEnableLoadMore(loadMoreEnabled())
//        smartRefresh?.setEnableAutoLoadMore(loadMoreEnabled())
        errorGroup = v.findViewById(R.id.error_group)
        retryBtn = v.findViewById(R.id.retry)
        recyclerView = v.findViewById(R.id.recyclerview)
        recyclerView?.adapter =
            adapter.withLoadStateFooter(ErrorLoadStateAdapter { adapter.retry() })

        retryBtn?.setOnClickListener {
            progress?.visible()
            errorGroup?.gone()
            adapter.retry()
        }

        lifecycleScope.launchWhenStarted {
//            showsViewModel.shows().collectLatest { adapter.submitData(it) }
        }
    }

    override fun loadData() {
        smartRefresh?.isRefreshing = true
        lifecycleScope.launchWhenStarted {
            mViewModel.loadData(initKey, pageSize, this@BasePagingFragment)
                .collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                    smartRefresh?.isRefreshing = false
                }
        }
    }

    open fun onMessage(event: MsgEvent<*>) {

    }

    open fun eventBusEnable(): Boolean {
        return false
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onReceiveMessage(event: MsgEvent<*>) {
        if (javaClass.simpleName.equals(event.from)) onMessage(event)
//        else if (Constant.ACTION_SCAN_CODE == event.receiver) {
//            if (event.`object` is String) {
//
//            }
//        }
    }

    open fun initOptions(vararg actions: Any) {
        MENUID_LOOKUP.clear()
//        if (actions.size > 2) {
//            Common.showToast("不能设置超过2个action")
//            return
//        }
        for ((index, action) in actions.withIndex()) {
            MENUID_LOOKUP.put(MENU_ITEM_ITEM1 + index, action)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        for (i in 0 until MENUID_LOOKUP.size()) {
            val key: Int = MENUID_LOOKUP.keyAt(i)
            val action = MENUID_LOOKUP.get(key).toString()
            if (!TextUtils.isEmpty(action)) {
                val item = menu.add(
                    if (i < 2) Menu.FIRST else Menu.NONE,
                    key,
                    Menu.CATEGORY_ALTERNATIVE,
                    action
                )
                item.setShowAsActionFlags(
                    if (i < 2) MenuItem.SHOW_AS_ACTION_ALWAYS else
                        MenuItem.SHOW_AS_ACTION_IF_ROOM
                )
            }
        }
    }

    open fun onOptionItemclicked(action: Any) {
        toast("onOptionItemclicked action= $action")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onOptionItemclicked(MENUID_LOOKUP.get(item.itemId))
        return true
    }
}

open class PagingViewModel : ViewModel() {


}

fun <K : Any, T : BaseModel> ViewModel.loadData(page: K?, pageSize: Int, fetchAction: FetchAction<K, T>): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize * 2,
            enablePlaceholders = true,
            prefetchDistance = 2,
        )
    ) {
        PageSource(page, fetchAction)
    }.flow.cachedIn(viewModelScope)
}

open class DiffUtilItemCallback<T : BaseModel> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.getItemId() == newItem.getItemId()
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.getItemContent() == newItem.getItemContent()
    }
}


//inline fun <reified DataClass : Any, Item : BaseItem> Fragment.pagingModel(
////    noinline loader: suspend () -> Status<DataClass>,
////    noinline nextLoader: suspend (String) -> Status<DataClass>,
////    noinline mapper: (DataClass) -> Item
//): Lazy<PagingViewModel> {
//    return this.viewModels(factoryProducer = {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return PagingViewModel() as T
//            }
//        }
//    })
//}