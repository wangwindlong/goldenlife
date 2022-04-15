package net.wangyl.goldenlife.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import net.wangyl.base.*
import net.wangyl.base.base.*
import net.wangyl.base.extension.checkContext
import net.wangyl.eventbus_flow.core.observeEvent
import net.wangyl.goldenlife.Constants.EVENT_LOGOUT
import net.wangyl.goldenlife.event.GlobalEvent
import net.wangyl.goldenlife.obj.Global.relaunchApp
import net.wangyl.goldenlife.ui.frag.HeadlinesFragment
import net.wangyl.goldenlife.ui.settings.SettingsFragment
import net.wangyl.goldenlife.ui.slideshow.SlideshowFragment

class HomeActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeEvent<GlobalEvent> { value ->
            if (EVENT_LOGOUT == value.name) {
                relaunchApp()
            }
        }
    }

}

fun Context.launchMain() {
    goHome(TabViewPagerFragment::class.java) {
        putExtra(TAG_DECORATE, true) //开启动态添加布局模式
        //子fragment的参数，包裹到TAG_ARGS下面的bundle中，由activity分发给创建的fragment
        putBundle(TAG_ARGS) {
//            putBoolean(TAG_DECORATE, true)
            putParcelableArrayList(TAG_FRAGS,
                arrayListOf(
                    fragmentPage(HeadlinesFragment::class.java, "标题1") {
                        putExtra("args_1", "args1")
                    }, fragmentPage(SlideshowFragment::class.java, "标题2") {
                        putExtra("args_1", "args2")
                    }, fragmentPage(SettingsFragment::class.java, "标题3") {
                        putExtra("args_1", "args2")
                    },
                ))
            putInt(TAG_TAB, FLAG_TABBAR_BTM)
        }
    }
//        startActivity(Intent(this, MainActivity::class.java))
}

fun Intent.putBundle(argTag: String, extra: Bundle.() -> Unit) : Intent {
    return putExtra(argTag, Bundle().apply(extra))
}

fun<T: Fragment> Context.goHome(frag : Class<T>, extra: Intent.() -> Unit) {
    checkContext(this)
    val intent = Intent(this, HomeActivity::class.java).apply {
        putExtras(apply(extra))
        putExtra(TAG_FRAGNAME, frag.name)
    }
    startActivity(intent)
}