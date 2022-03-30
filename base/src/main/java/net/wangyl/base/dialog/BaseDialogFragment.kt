package net.wangyl.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

open class BaseDialogFragment(var layout: Int = 0) : CoroutineDialogFragment<Int>(layout) {

    open fun initLayout(): Int {
        return if (layout == 0) net.wangyl.base.R.layout.base_layout_empty else layout
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutid = initLayout()
        if (layoutid == 0) throw IllegalArgumentException("must pass layoutid or overide initLayout method")
        return inflater.inflate(layoutid, container, false)
    }


}