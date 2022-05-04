package net.wangyl.base.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import timber.log.Timber

open class BaseBindFragment<T : ViewBinding> : BaseFragment() {
    lateinit var binding: T

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
       binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }
}