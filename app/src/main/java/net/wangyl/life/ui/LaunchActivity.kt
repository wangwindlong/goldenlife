package net.wangyl.life.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.wangyl.base.extension.startActivity
import net.wangyl.life.compose.ui.MainActivity
import net.wangyl.life.obj.Global
import timber.log.Timber

class LaunchActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // https://code.google.com/p/android/issues/detail?id=26658
        if (!isTaskRoot) {
            finish()
            return
        }

        Timber.d("LaunchActivity onCreate userSession=${Global.userSession}")
//        if (!Global.isLogin()) {
//            goSimpleActivity(LoginCompose::class.java)
//        } else {
//            launchMain()
//        }
        startActivity<MainActivity>()
        finish()
    }
}





