package net.wangyl.base.mvi.orbit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import net.wangyl.base.data.BaseModel


/**
 * 是否可以直接通过代理方式 实现binding创建和传入contentview，见com.dylanc.viewbinding
 */
open class BindMviFragment<Data : BaseModel, DB: ViewDataBinding> : BaseMviFragment<Data>() {

    lateinit var binding: DB

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }


}