package net.wangyl.goldenlife.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import net.wangyl.goldenlife.GoldApp
import net.wangyl.goldenlife.R
import org.koin.android.ext.android.inject
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
    Toast.makeText(GoldApp.sInstance, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String?) {
    Toast.makeText(GoldApp.sInstance, message, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(@StringRes message: Int) {
    Toast.makeText(GoldApp.sInstance, message, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(message: String?) {
    Toast.makeText(GoldApp.sInstance, message, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> Fragment.startActivity() {
    startActivity(Intent(context, T::class.java))
}

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}