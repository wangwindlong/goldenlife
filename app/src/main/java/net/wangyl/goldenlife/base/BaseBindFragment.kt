package net.wangyl.goldenlife.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import timber.log.Timber

open class BaseBindFragment<T : ViewBinding> : BaseFragment() {
    lateinit var binding: T

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        Timber.d("createView inflater=$inflater ，getLayoutId()=${getLayoutId()}, container=$container")
       binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }
}