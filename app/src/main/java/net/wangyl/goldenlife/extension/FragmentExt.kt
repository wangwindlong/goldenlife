package net.wangyl.goldenlife.extension

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.viewbinding.ViewBinding
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.mvi.base.BaseListVM
import net.wangyl.goldenlife.mvi.base.BaseVM

//
//
//inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(this) { view: View ->
//    T::class.java.getMethod("bind", View::class.java).invoke(null, view) as T
//}





fun Fragment.navTo(navId: Int, bundle: Bundle? = null,
                   navOptions: NavOptions? = NavOptions.Builder().setHorizontalSlide().build(),
                   extras: FragmentNavigator.Extras? = null) {
    findNavController().navigate(navId, bundle, navOptions, extras)
}