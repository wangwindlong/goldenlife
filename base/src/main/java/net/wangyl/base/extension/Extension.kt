package net.wangyl.base.extension

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.view.View
import android.view.ViewManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavOptions
import net.wangyl.base.R
import net.wangyl.base.SimpleActivity
import net.wangyl.base.TAG_FRAGNAME
import net.wangyl.base.data.FragmentData
import net.wangyl.base.enums.LoadingState
import net.wangyl.base.fragmentPage
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent
import timber.log.Timber
import java.time.Duration

//</editor-fold>
private val density = Resources.getSystem().displayMetrics.density

inline fun <reified T> getK(qualifier: Qualifier? = null): T {
    return KoinJavaComponent.get(T::class.java, qualifier)
}

fun NavOptions.Builder.setHorizontalSlide(): NavOptions.Builder {
    return setEnterAnim(R.anim.base_h_slide_enter)
        .setExitAnim(R.anim.base_h_slide_exit)
        .setPopEnterAnim(R.anim.base_h_slide_popenter)
        .setPopExitAnim(R.anim.base_h_slide_popexit)
}

inline fun<T> goIntent(c: Context, frag: Class<T>, init: Intent.() -> Unit): Intent {
    Timber.d("goIntent fragName= ${frag.name}")
    return Intent(c, SimpleActivity::class.java).apply { init() }.putExtra(TAG_FRAGNAME, frag.name)
}


fun Context.createFragment(fm: FragmentManager, data: FragmentData) : Fragment {
    return fm.fragmentFactory.instantiate(classLoader, data.name).apply {
        arguments = data.extra?.extras
    }
}

fun Context.isDebuggable(): Boolean = (0 != (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Int.px2dp(): Int {
    return (this / density).toInt()
}

//使用值对应的dp
fun Int.toPx(): Float {
    return 0.5f + this * density
}

fun Int.dp2px(): Int {
    return this.toPx().toInt()
}

fun checkContext(context: Context) {
    if (context is Application)
        throw IllegalStateException("please call this in activity or fragment or view")
}

fun<T: Fragment>  Context.goSimpleActivity(frag: Class<T>, extra: Intent? = null) {
    checkContext(this)
    startActivity(goIntent(this, frag) {
        extra?.let { this.putExtras(it) }
    })
}

fun<T: Fragment> Fragment.goSimpleActivity(frag: Class<T>, extra: Intent? = null) {
    startActivity(goIntent(requireContext(), frag) {
        extra?.let { this.putExtras(it) }
    })
}

fun Fragment.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context?.applicationContext, message, duration).show()
}

fun Fragment.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context?.applicationContext, message, duration).show()
}

fun Activity.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}

fun Activity.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(applicationContext, message, duration).show()
}

inline fun <reified T> Fragment.startActivity() {
    startActivity(Intent(context, T::class.java))
}

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}