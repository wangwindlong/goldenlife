package net.wangyl.goldenlife.mvi.base

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.extension.getK
import net.wangyl.goldenlife.model.BaseModel
import net.wangyl.goldenlife.model.PostData

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BaseMviFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class BaseMviFragment<Data : BaseModel> : Fragment(), RefreshEvent {
    val TAG = javaClass.simpleName
    private var param1: String? = null
    private var param2: String? = null
    val vm by mviViewModel<Data>(this) {
        onVMInit()
    }

    open fun onVMInit() {

    }

    open fun getLayoutId() :Int {
        return R.layout.fragment_common_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
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