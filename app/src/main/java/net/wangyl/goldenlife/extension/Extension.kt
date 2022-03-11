package net.wangyl.goldenlife.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavOptions
import net.wangyl.goldenlife.GoldApp
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.base.FragmentData
import net.wangyl.goldenlife.base.TAG_FRAGNAME
import net.wangyl.goldenlife.ui.SimpleActivity
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent
import timber.log.Timber

//</editor-fold>
private val density = Resources.getSystem().displayMetrics.density

inline fun <reified T> getK(qualifier: Qualifier? = null): T {
    return KoinJavaComponent.get(T::class.java, qualifier)
}

fun NavOptions.Builder.setHorizontalSlide(): NavOptions.Builder {
    return setEnterAnim(R.anim.h_slide_enter)
        .setExitAnim(R.anim.h_slide_exit)
        .setPopEnterAnim(R.anim.h_slide_popenter)
        .setPopExitAnim(R.anim.h_slide_popexit)
}

fun goIntent(ctx: Context?, fragName: String, extra: Intent? = null): Intent {
    Timber.d("goIntent fragName= $fragName")
    return Intent(ctx, SimpleActivity::class.java).apply {
        extra?.let { putExtras(it) }
        putExtra(TAG_FRAGNAME, fragName)
    }
}


fun Context.createFragment(fm: FragmentManager, data: FragmentData) : Fragment {
    return fm.fragmentFactory.instantiate(classLoader, data.name).apply {
        arguments = data.extra?.extras
    }
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