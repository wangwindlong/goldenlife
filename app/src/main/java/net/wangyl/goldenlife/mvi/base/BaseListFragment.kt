package net.wangyl.goldenlife.mvi.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.extension.toast
import net.wangyl.goldenlife.base.BaseItem
import net.wangyl.goldenlife.base.BaseModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber

interface RefreshEvent {
    fun refresh(isManualRefresh: Boolean)
}

val defaultItem = R.layout.item_text_view


class PageInfo {
    var page = 0
    var pageSize = 10
    var nextParams = ""
    fun nextPage() {
        page++
    }

    fun reset() {
        page = 0
    }

    val isFirstPage: Boolean
        get() = page == 0
}

abstract class BaseListFragment<Data : BaseModel> :
    BaseMviFragment<Data>(), IBindItem<Data, MyBaseViewHolder> {
//    private val refreshViewModel: RefreshViewModel by viewModels()
//    protected val wtfViewModel: WTFViewModel by viewModels()

    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var emptyView: View
    private var initialised: Boolean = false

    private var adapter = BaseMultiAdapter(getItemLayouts(), this)

//    override fun onVMInit() {
//        refresh(true)
//    }

    //    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    fun getItemLayouts(): List<Int> {
        return ArrayList<Int>().apply { add(defaultItem) }
    }

    abstract suspend fun loader(params: PageInfo): Status<List<Data>>

//    private val binding by viewBinding<FragmentCommonListBinding>()

    override fun refresh(isManualRefresh: Boolean) {
        adapter.loadMoreModule.isEnableLoadMore = false
        vm.pageInfo.reset()
        loadList(vm.pageInfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        refreshLayout = view!!.findViewById(R.id.refresh_layout)
        recyclerView = view.findViewById(R.id.list_view)
//        refreshLayout = binding.refreshLayout
//        progressBar = binding.progressCircular
        refreshLayout.setOnRefreshListener {
            refresh(true)
        }

        initRV(view)
        vm.observe(viewLifecycleOwner, state = ::render, sideEffect = ::sideEffect)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    private fun initRV(view: View) {
        emptyView = layoutInflater.inflate(R.layout.layout_empty, view as ViewGroup, false)
        emptyView.findViewById<View>(R.id.refresh_btn).setOnClickListener {
//            refreshLayout.isRefreshing = true
            refresh(true)
//            toast("刷新")
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        adapter.animationEnable = true
        adapter.setEmptyView(emptyView)
//        recyclerView.apply {
//            adapter = groupAdapter
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            itemAnimator = null
//            SeparatorDecoration(requireActivity(), R.dimen.separator_margin_start_icon, R.dimen.separator_margin_end)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//        }

        adapter.loadMoreModule.setOnLoadMoreListener {
            loadList(vm.pageInfo) //请求下一页数据
        }
        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                vm.onItemClicked(adapter.data.get(position) as Data)
            }

        })
        refresh(true)
    }

    fun loadList(pageInfo: PageInfo) {
        refreshLayout.isRefreshing = true
        vm.intent(registerIdling = false) {
            //这里的参数使用封装过的类,便于传其他类型的下一页数据
            val status = loader(pageInfo)
            Timber.d("loadList finished state=${state}")
            reduce {
                when (status) {
                    //需要在原有数据上添加
                    is Status.Success -> {
                        val newdata = status.data
                        val newstate: BaseState<Data>
                        //如果为第一次加载，则直接返回
                        if (pageInfo.isFirstPage) {
                            newstate = state.copy(values = newdata, error = null, isEnd = false,
                                _count = state._count + 1
                            )
                        } else {
                            //如果长度为0或不足pagesize，则返回已加载完成
                            newstate = state.copy(
                                values = state.values + newdata,
                                error = null,
                                isEnd = newdata.isEmpty() || newdata.size < pageInfo.pageSize,
                                _count = state._count + 1
                            )
                        }
                        pageInfo.nextPage()//请求成功计数+1,更新下一页参数 nextParams
                        newstate
                    }
                    //如果加载失败,返回错误信息
                    is Status.Failure -> state.copy(error = status.exception)

                }

            }
        }
    }

    fun render(state: BaseState<Data>) {
//        val items = state.values.map { value ->
//            BaseListItem(value, listViewModel)
//        }
//
//        groupAdapter.update(items)
        Timber.d("render pageinfo=${vm.pageInfo.page} itemcounts=${state.values.size} initialised=${initialised}")
        if (!initialised) { //去掉第一次初始化的回调, 主要为退出页面后再进来会再次调用该方法
            initialised = true
            adapter.setList(state.values)
            refreshLayout.isRefreshing = state.values.isEmpty()
            return
        }
        if (state.error != null) {
            if (!vm.pageInfo.isFirstPage) adapter.loadMoreModule.loadMoreFail()
//            toast("加载出错 ${state.error}")
        } else {
            adapter.setList(state.values)
            if (state.isEnd) {
//                toast("已加载完成所有")
                adapter.loadMoreModule.loadMoreEnd()
            } else {
//                toast("已加载完当前页")
            }
        }
        adapter.loadMoreModule.loadMoreComplete()
        refreshLayout.isRefreshing = false
        adapter.loadMoreModule.isEnableLoadMore = true
    }

    //跳转详情
    open fun sideEffect(event: Event) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView pageinfo=${vm.pageInfo.page}")
//        vm.intent {
//            reduce {
//                Log.d(
//                    TAG,
//                    "onDestroyView intent count=${state.values.size} isFirst=${state.isFirst}"
//                )
//                state.copy(isFirst = true)
//            }
//        }
    }
}



class MyBaseViewHolder(view: View) : BaseViewHolder(view) {
    var dataBinding: ViewDataBinding? = null
}

class MultiTypeDelegate<Data>(layouts: List<Int>) : BaseMultiTypeDelegate<Data>() {
    init {
        layouts.map { addItemType(it, it) }
    }

    override fun getItemType(data: List<Data>, position: Int): Int {
        val item = data[position % data.size]
        return if (item is BaseItem) item.getItemType() else defaultItem
    }
}


//inline fun <reified DataClass : Parcelable> Fragment.listViewModel(
//    noinline loader: suspend () -> Status<List<DataClass>>,
//    noinline nextLoader: suspend (String) -> Status<List<DataClass>>,
////    noinline mapper: (DataClass) -> ItemHolder
//): Lazy<BaseListVM<DataClass>> {
//    return this.viewModels(factoryProducer = {
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                return BaseListVM(loader, save, get()) as T
//            }
//        }
//    })
//}

