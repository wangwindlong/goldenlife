package net.wangyl.goldenlife.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.SavedStateRegistryOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.listener.OnLoadMoreListener
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.base.BaseMultiAdapter
import net.wangyl.goldenlife.base.IBindItem
import net.wangyl.goldenlife.databinding.FragmentCommonListBinding
import net.wangyl.goldenlife.extension.viewBinding
import net.wangyl.goldenlife.model.BaseItem
import net.wangyl.goldenlife.model.BaseModel
import net.wangyl.goldenlife.mvi.BaseListVM
import net.wangyl.goldenlife.mvi.BaseState
import net.wangyl.goldenlife.mvi.DertailEvent
import net.wangyl.goldenlife.mvi.Event
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.get
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.observe

interface RefreshEvent {
    fun refresh(view: View, isManualRefresh: Boolean)
}

val defaultItem = R.layout.item_text_view

abstract class BaseListFragment<Data : BaseModel>(layoutId: Int = R.layout.fragment_common_list) :
    Fragment(layoutId), RefreshEvent, IBindItem<Data, MyBaseViewHolder> {
    private val TAG = javaClass.simpleName
//    private val refreshViewModel: RefreshViewModel by viewModels()
//    protected val wtfViewModel: WTFViewModel by viewModels()

    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var emptyView: View

    private var adapter = BaseMultiAdapter(getItemLayouts(), this)

    val listModel by viewModels<BaseListVM<Data>> {
        MyViewModelFactory(this, loader = ::loader)
    }

    //    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    // 可以直接快速使用，也可以继承BaseBinderAdapter类，重写自己的相关方法

    @CallSuper
    fun getItemLayouts(): List<Int> {
        return ArrayList<Int>().apply { add(defaultItem) }
    }

    abstract suspend fun loader(): Status<List<Data>>

    private val binding by viewBinding<FragmentCommonListBinding>()

    override fun refresh(view: View, isManualRefresh: Boolean) {
        listModel.intent { loader() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view.findViewById(R.id.refresh_layout)
        recyclerView = view.findViewById(R.id.list_view)
//        refreshLayout = binding.refreshLayout
//        progressBar = binding.progressCircular
        refreshLayout.setOnRefreshListener {
            refresh(view, true)
        }

        initRV()
        listModel.observe(viewLifecycleOwner, state = ::render, sideEffect = ::sideEffect)
    }

    private fun initRV() {
        emptyView = layoutInflater.inflate(R.layout.layout_empty, view as ViewGroup, false)
        emptyView.findViewById<View>(R.id.refresh_btn).setOnClickListener {
//            refreshLayout.isRefreshing = true
            refresh(it, true)
            Toast.makeText(context, "刷新", Toast.LENGTH_LONG).show()
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

        adapter.loadMoreModule.setOnLoadMoreListener(OnLoadMoreListener { loadMore() })
        adapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        adapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
    }

    fun loadMore() {
        Toast.makeText(context, "加载更多", Toast.LENGTH_LONG).show()
    }

    fun render(state: BaseState<Data>) {
//        val items = state.values.map { value ->
//            BaseListItem(value, listViewModel)
//        }
//
//        groupAdapter.update(items)
        refreshLayout.isRefreshing = false
//        adapter.setList(state.values)

    }

    //跳转详情
    private fun sideEffect(sideEffect: Event) {
        when (sideEffect) {
            is DertailEvent<*> -> {
                findNavController().navigate(R.id.nav_settings)
//                findNavController().navigate(
//                    PostListFragmentDirections.actionListFragmentToDetailFragment(
//                        sideEffect.post
//                    )
//                )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        binding = null
    }
}



class MyViewModelFactory<DataClass : Parcelable>(
    owner: SavedStateRegistryOwner,
    val loader: (suspend () -> Status<List<DataClass>>),
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val repository: Repository = getK()
        return BaseListVM(loader, handle, repository) as T
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

inline fun <reified T> getK(qualifier: Qualifier? = null): T {
    return get(T::class.java, qualifier)
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