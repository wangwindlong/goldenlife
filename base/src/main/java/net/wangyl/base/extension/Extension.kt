package net.wangyl.base.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import net.wangyl.base.R
import net.wangyl.base.SimpleActivity
import net.wangyl.base.data.FragmentData
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent
import timber.log.Timber

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

fun goIntent(ctx: Context?, fragName: String, extra: Intent? = null): Intent {
    Timber.d("goIntent fragName= $fragName")
    return Intent(ctx, SimpleActivity::class.java).apply {
        extra?.let { putExtras(it) }
        putExtra(net.wangyl.base.TAG_FRAGNAME, fragName)
    }
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

fun Int.px(): Int {
    return (this / density).toInt()
}

//使用值对应的dp
fun Int.dp(): Int {
    return (0.5f + this * density).toInt()
}

fun Activity.goActivity(frag: Class<Fragment>, extra: Intent? = null) {
    startActivity(goIntent(this, frag.name, extra))
}

fun Activity.goActivity(fragName: String, extra: Intent? = null) {
    startActivity(goIntent(this, fragName, intent))
}

fun Fragment.goActivity(frag: Class<Fragment>, extra: Intent? = null) {
    startActivity(goIntent(context, frag.name, extra ?: Intent().replaceExtras(arguments)))
}

fun Fragment.goActivity(fragName: String, extra: Intent? = null) {
    startActivity(goIntent(context, fragName, extra ?: Intent().replaceExtras(arguments)))
}

fun Fragment.toast(@StringRes message: Int) {
    Toast.makeText(context?.applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String?) {
    Toast.makeText(context?.applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(@StringRes message: Int) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(message: String?) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> Fragment.startActivity() {
    startActivity(Intent(context, T::class.java))
}

inline fun <reified T> Context.startActivity() {
    startActivity(Intent(this, T::class.java))
}