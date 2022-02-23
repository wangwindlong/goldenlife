package net.wangyl.goldenlife.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.SavedStateRegistryOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.databinding.FragmentCommonListBinding
import net.wangyl.goldenlife.extension.viewBinding
import net.wangyl.goldenlife.mvi.BaseListVM
import net.wangyl.goldenlife.mvi.BaseState
import net.wangyl.goldenlife.ui.common.SeparatorDecoration
import net.wangyl.goldenlife.ui.groupie.BaseListItem
import net.wangyl.goldenlife.ui.widget.ProgressImageButton
import org.koin.java.KoinJavaComponent.get
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.observe

interface RefreshEvent {
    fun refresh(view: View, isManualRefresh: Boolean)
}

abstract class BaseListFragment<Data : Parcelable>(layoutId: Int = R.layout.fragment_common_list) :
    Fragment(layoutId), RefreshEvent {
//    private val refreshViewModel: RefreshViewModel by viewModels()
//    protected val wtfViewModel: WTFViewModel by viewModels()

    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressImageButton
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

    val listModel by viewModels<BaseListVM<Data>> {
        MyViewModelFactory(this, loader = ::loader)
    }

    abstract suspend fun loader(): Status<List<Data>>

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

        recyclerView.apply {
            adapter = groupAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = null
            SeparatorDecoration(requireActivity(), R.dimen.separator_margin_start_icon, R.dimen.separator_margin_end)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        listModel.observe(viewLifecycleOwner, state = ::render, sideEffect = ::sideEffect)

    }

    fun render(state: BaseState<Data>) {
        val items = state.values.map { value ->
            BaseListItem(value, listViewModel)
        }

        groupAdapter.update(items)
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
    ): T = BaseListVM(loader, handle) as T
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