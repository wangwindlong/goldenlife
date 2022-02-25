package net.wangyl.goldenlife.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.databinding.FragmentCommonListBinding
import net.wangyl.goldenlife.extension.viewBinding
import net.wangyl.goldenlife.model.BaseItem
import net.wangyl.goldenlife.model.BaseModel
import net.wangyl.goldenlife.mvi.BaseListVM
import net.wangyl.goldenlife.mvi.BaseState
import net.wangyl.goldenlife.mvi.DertailEvent
import net.wangyl.goldenlife.mvi.Event
import net.wangyl.goldenlife.ui.widget.ProgressImageButton
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.get
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.observe

interface RefreshEvent {
    fun refresh(view: View, isManualRefresh: Boolean)
}

val defaultItem = R.layout.item_text_view

abstract class BaseListFragment<Data : BaseModel>(layoutId: Int = R.layout.fragment_common_list) :
    Fragment(layoutId), RefreshEvent {
//    private val refreshViewModel: RefreshViewModel by viewModels()
//    protected val wtfViewModel: WTFViewModel by viewModels()

    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressImageButton
    var itemLayouts: ArrayList<Int> = arrayListOf()

    val listModel by viewModels<BaseListVM<Data>> {
        MyViewModelFactory(this, loader = ::loader)
    }

    //    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    // 可以直接快速使用，也可以继承BaseBinderAdapter类，重写自己的相关方法
    private var adapter: BaseDelegateMultiAdapter<Data, MyBaseViewHolder> =
        object : BaseDelegateMultiAdapter<Data, MyBaseViewHolder>() {

        }.apply {
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



    @CallSuper
    fun getItemLayouts(): List<Int> {
        return ArrayList<Int>().apply { add(defaultItem) }
    }

    abstract suspend fun loader(): Status<List<Data>>
    abstract fun bindItem(holder: MyBaseViewHolder, item: BaseModel, payloads: List<Any>? = null)


    private val binding by viewBinding<FragmentCommonListBinding>()

    override fun refresh(view: View, isManualRefresh: Boolean) {
        listModel.intent { loader() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view.findViewById(R.id.refresh_layout)
        progressBar = view.findViewById(R.id.progress_circular)
        recyclerView = view.findViewById(R.id.list_view)
//        refreshLayout = binding.refreshLayout
//        progressBar = binding.progressCircular
        refreshLayout.setOnRefreshListener {
            refresh(view, true)
        }

        val emptyView = view.findViewById<FrameLayout>(R.id.empty_image)
        if (emptyView != null) {
//            refreshViewModel.showEmptyView.observe(viewLifecycleOwner, Observer {
//                emptyView.visibleOrGone = it
//            })
        }

        initRV()
        listModel.observe(viewLifecycleOwner, state = ::render, sideEffect = ::sideEffect)

    }

    private fun initRV() {
        itemLayouts.clear()
        itemLayouts.addAll(getItemLayouts())

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
//        recyclerView.apply {
//            adapter = groupAdapter
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            itemAnimator = null
//            SeparatorDecoration(requireActivity(), R.dimen.separator_margin_start_icon, R.dimen.separator_margin_end)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//        }
    }

    fun render(state: BaseState<Data>) {
//        val items = state.values.map { value ->
//            BaseListItem(value, listViewModel)
//        }
//
//        groupAdapter.update(items)
        adapter.setList(state.values)
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

class MyBaseViewHolder(view: View): BaseViewHolder(view) {

    var dataBinding: ViewDataBinding? = null
}

class DelegateMultiAdapter<Data,VH:BaseViewHolder>:BaseDelegateMultiAdapter<Data,VH> {
    constructor(layouts: List<Int>) {
        setMultiTypeDelegate(MultiTypeDelegate(layouts))
    }
    override fun convert(holder: MyBaseViewHolder, item: Data) {
        bindItem(holder, item)
    }

    override fun convert(holder: MyBaseViewHolder, item: Data, payloads: List<Any>) {
        bindItem(holder, item, payloads)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): MyBaseViewHolder {
        val viewHolder = super.onCreateDefViewHolder(parent, viewType)
        viewHolder.dataBinding = DataBindingUtil.bind(parent)
        return viewHolder
    }

}

class MultiTypeDelegate<Data>: BaseMultiTypeDelegate<Data> {
    constructor(layouts: List<Int>) {
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