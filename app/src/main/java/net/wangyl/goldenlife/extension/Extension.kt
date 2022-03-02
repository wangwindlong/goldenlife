package net.wangyl.goldenlife.extension

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import net.wangyl.goldenlife.R
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

inline fun <reified T> getK(qualifier: Qualifier? = null): T {
    return KoinJavaComponent.get(T::class.java, qualifier)
}

fun NavOptions.Builder.setHorizontalSlide(): NavOptions.Builder {
    return setEnterAnim(R.anim.h_slide_enter)
        .setExitAnim(R.anim.h_slide_exit)
        .setPopEnterAnim(R.anim.h_slide_popenter)
        .setPopExitAnim(R.anim.h_slide_popexit)
}


fun Fragment.toast(@StringRes message: Int) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(@StringRes message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}