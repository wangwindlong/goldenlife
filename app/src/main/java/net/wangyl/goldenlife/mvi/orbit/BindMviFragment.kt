package net.wangyl.goldenlife.mvi.orbit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import net.wangyl.goldenlife.base.BaseModel


/**
 * A simple [Fragment] subclass.
 * Use the [BaseMviFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class BindMviFragment<Data : BaseModel, DB: ViewDataBinding> : BaseMviFragment<Data>() {

    lateinit var binding: DB

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }


}