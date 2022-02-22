package net.wangyl.goldenlife.extension

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
//
//
//inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(this) { view: View ->
//    T::class.java.getMethod("bind", View::class.java).invoke(null, view) as T
//}