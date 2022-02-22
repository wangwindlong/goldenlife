package net.wangyl.goldenlife.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.databinding.FragmentCommonListBinding
import net.wangyl.goldenlife.extension.viewBinding
import net.wangyl.goldenlife.mvi.BaseListVM
import net.wangyl.goldenlife.ui.widget.ProgressImageButton
import org.koin.java.KoinJavaComponent.get

interface RefreshEvent {
    fun refresh(view: View, isManualRefresh: Boolean)
}

class BaseListFragment<Data:Parcelable>(layoutId: Int) : Fragment(layoutId), RefreshEvent {
//    private val refreshViewModel: RefreshViewModel by viewModels()
//    protected val wtfViewModel: WTFViewModel by viewModels()

    lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var progressBar: ProgressImageButton
    val listModel by viewModels<BaseListVM<Data>> {
        MyViewModelFactory(this, loader = ::loader)
    }

    suspend fun loader(): Status<List<Data>>{

    }

//    private val binding by viewBinding<FragmentCommonListBinding>()

    override fun refresh(view: View, isManualRefresh: Boolean) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshLayout = view.findViewById(R.id.refresh_layout)
        progressBar = view.findViewById(R.id.progress_circular)
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
    }
}

class MyViewModelFactory<DataClass:Parcelable>(owner: SavedStateRegistryOwner,
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