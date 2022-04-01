package net.wangyl.goldenlife.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.wangyl.base.*
import net.wangyl.base.extension.goSimpleActivity
import net.wangyl.goldenlife.MainActivity
import net.wangyl.goldenlife.ui.slideshow.SlideshowFragment

class LaunchActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // https://code.google.com/p/android/issues/detail?id=26658
        if (!isTaskRoot) {
            finish()
            return
        }
//        goHome(
//            TabViewPagerFragment::class.java, Intent().putParcelableArrayListExtra(TAG_FRAGS,
//                arrayListOf(fragmentPage(SlideshowFragment::class.java, "标题1") {
//                    putExtra("args_1", "args1")
//                }, fragmentPage(SlideshowFragment::class.java, "标题2") {
//                    putExtra("args_1", "args2")
//                })
//            ).putExtra(TAG_TAB, FLAG_TABBAR_Botom)
//        )
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}