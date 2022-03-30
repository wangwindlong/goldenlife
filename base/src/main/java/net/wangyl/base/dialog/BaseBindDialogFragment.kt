package net.wangyl.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

open class BaseBindDialogFragment<T : ViewBinding>(layout: Int = 0) :
    BaseDialogFragment(layout) {

    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (layout != 0)
            binding = DataBindingUtil.inflate(inflater, initLayout(), container, false)
        return binding.root
    }


}