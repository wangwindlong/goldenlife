package net.wangyl.base.mvi.orbit

import android.os.Bundle
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
import net.wangyl.base.data.BaseItem
import net.wangyl.base.data.BaseModel
import net.wangyl.base.R
import net.wangyl.base.data.ApiResponse
import timber.log.Timber

val defaultItem = R.layout.base_item_text_view

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

abstract class BaseMviListFragment<Data : BaseModel> :
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

    abstract suspend fun loader(params: PageInfo): ApiResponse<List<Data>>

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

        Timber.d("onCreateView")
        initRV(view)
//        vm.observe(viewLifecycleOwner, state = ::render, sideEffect = ::sideEffect)
        return view
    }

    override fun loadData() {
        refresh(true)
    }

    private fun initRV(view: View) {
        emptyView = layoutInflater.inflate(R.layout.base_layout_empty, view as ViewGroup, false)
        emptyView.findViewById<View>(R.id.base_refresh_btn).setOnClickListener {
//            refreshLayout.isRefreshing = true
            refresh(true)
//            toast("??????")
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
            loadList(vm.pageInfo) //?????????????????????
        }
        adapter.loadMoreModule.isAutoLoadMore = true
        //??????????????????????????????????????????????????????????????????????????????????????????(?????????true)
        adapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                vm.onItemClicked(adapter.data.get(position) as Data)
            }

        })
    }

    /**
     * ???????????? mvi???????????????????????????mvi?????????mvrx or orbit???
     */
    fun loadList(pageInfo: PageInfo) {
        refreshLayout.isRefreshing = false
    }
    //orbit mvi logic
//    fun loadList(pageInfo: PageInfo) {
//        refreshLayout.isRefreshing = true
//        vm.intent(registerIdling = false) {
//            //????????????????????????????????????,???????????????????????????????????????
//            val status = loader(pageInfo)
//            Timber.d("loadList finished state=${state}")
//            reduce {
//                when (status) {
//                    //??????????????????????????????
//                    is Status.Success -> {
//                        val newdata = status.data
//                        val newstate: BaseState<Data>
//                        //??????????????????????????????????????????
//                        if (pageInfo.isFirstPage) {
//                            newstate = state.copy(values = newdata, error = null, isEnd = false,
//                                _count = state._count + 1
//                            )
//                        } else {
//                            //???????????????0?????????pagesize???????????????????????????
//                            newstate = state.copy(
//                                values = state.values + newdata,
//                                error = null,
//                                isEnd = newdata.isEmpty() || newdata.size < pageInfo.pageSize,
//                                _count = state._count + 1
//                            )
//                        }
//                        pageInfo.nextPage()//??????????????????+1,????????????????????? nextParams
//                        newstate
//                    }
//                    //??????????????????,??????????????????
//                    is Status.Failure -> state.copy(error = status.exception)
//
//                }
//
//            }
//        }
//    }

    fun render(state: BaseState<Data>) {
//        val items = state.values.map { value ->
//            BaseListItem(value, listViewModel)
//        }
//
//        groupAdapter.update(items)
        Timber.d("render pageinfo=${vm.pageInfo.page} itemcounts=${state.values.size} initialised=${initialised}")
        if (!initialised) { //?????????????????????????????????, ?????????????????????????????????????????????????????????
            initialised = true
            adapter.setList(state.values)
            refreshLayout.isRefreshing = state.values.isEmpty()
            return
        }
        if (state.error != null) {
            if (!vm.pageInfo.isFirstPage) adapter.loadMoreModule.loadMoreFail()
//            toast("???????????? ${state.error}")
        } else {
            adapter.setList(state.values)
            if (state.isEnd) {
//                toast("?????????????????????")
                adapter.loadMoreModule.loadMoreEnd()
            } else {
//                toast("?????????????????????")
            }
        }
        adapter.loadMoreModule.loadMoreComplete()
        refreshLayout.isRefreshing = false
        adapter.loadMoreModule.isEnableLoadMore = true
    }

    //????????????
    open fun sideEffect(event: Event) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView pageinfo=${vm.pageInfo.page}")
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
        //??????position ?????????item?????????layoutid
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

