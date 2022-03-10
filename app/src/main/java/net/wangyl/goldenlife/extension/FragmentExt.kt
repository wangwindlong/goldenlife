package net.wangyl.goldenlife.extension

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController

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