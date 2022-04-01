package net.wangyl.goldenlife.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import net.wangyl.base.extension.checkContext


fun<T: Fragment> Context.goHome(frag : Class<T>, extra: Intent? = null) {
    checkContext(this)
    val intent = Intent(this, HomeActivity::class.java).apply {
        putExtras(extra ?: Intent())
        putExtra(net.wangyl.base.TAG_FRAGNAME, frag.name)
    }
    startActivity(intent)
}