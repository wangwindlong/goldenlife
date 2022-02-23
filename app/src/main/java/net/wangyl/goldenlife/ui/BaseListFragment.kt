package net.wangyl.goldenlife.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.savedstate.SavedStateRegistryOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.databinding.FragmentCommonListBinding
import net.wangyl.goldenlife.extension.viewBinding
import net.wangyl.goldenlife.mvi.BaseListVM
import net.wangyl.goldenlife.mvi.BaseState
import net.wangyl.goldenlife.mvi.DertailEvent
import net.wangyl.goldenlife.mvi.Event
import net.wangyl.goldenlife.ui.common.SeparatorDecoration
import net.wangyl.goldenlife.ui.widget.ProgressImageButton
import org.koin.java.KoinJavaComponent.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent.inject
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
//    private val groupAdapter = GroupAdapter<GroupieViewHolder>()

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

//        recyclerView.apply {
//            adapter = groupAdapter
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            itemAnimator = null
//            SeparatorDecoration(requireActivity(), R.dimen.separator_margin_start_icon, R.dimen.separator_margin_end)
//            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//        }

        listModel.observe(viewLifecycleOwner, state = ::render, sideEffect = ::sideEffect)

    }

    fun render(state: BaseState<Data>) {
//        val items = state.values.map { value ->
//            BaseListItem(value, listViewModel)
//        }
//
//        groupAdapter.update(items)
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

inline fun <reified T> getK(qualifier: Qualifier? = null):T {
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