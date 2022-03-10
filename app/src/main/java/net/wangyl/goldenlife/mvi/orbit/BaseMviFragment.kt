package net.wangyl.goldenlife.mvi.orbit

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import net.wangyl.goldenlife.base.BaseFragment
import net.wangyl.goldenlife.base.BaseModel
import net.wangyl.goldenlife.utils.manager.EventBusManager

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BaseMviFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class BaseMviFragment<Data : BaseModel> : BaseFragment() {

    val eventBus = EventBusManager.get()
    private var param1: String? = null
    private var param2: String? = null
    val vm by mviViewModel<BaseVM<Data>, Data>(this) {
//        this.onVMInit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BaseMviFragment<BaseModel>().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun refresh(isManualRefresh: Boolean) {

    }
}

inline fun <reified VM : BaseVM<DataClass>, DataClass : Parcelable> Fragment.mviViewModel(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null,
    noinline onCreate: (VM.() -> Unit)? = null,
): Lazy<VM> {
    return this.viewModels(factoryProducer = {
        object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val clz = VM::class.java
                val mCreate = clz.getDeclaredConstructor(handle::class.java)
                mCreate.isAccessible = true
                return mCreate.newInstance(handle) as T
//                return VM(handle).apply {
//                    onInit = onCreate
//                } as T
            }
        }
    })
}