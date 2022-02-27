package net.wangyl.goldenlife.extension

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.viewbinding.ViewBinding
import net.wangyl.goldenlife.R

//
//
//inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(this) { view: View ->
//    T::class.java.getMethod("bind", View::class.java).invoke(null, view) as T
//}


fun NavOptions.Builder.setHorizontalSlide(): NavOptions.Builder {
    return setEnterAnim(R.anim.h_slide_enter)
        .setExitAnim(R.anim.h_slide_exit)
        .setPopEnterAnim(R.anim.h_slide_popenter)
        .setPopExitAnim(R.anim.h_slide_popexit)
}