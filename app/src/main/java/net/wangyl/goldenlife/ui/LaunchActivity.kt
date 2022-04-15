package net.wangyl.goldenlife.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.wangyl.base.extension.goSimpleActivity
import net.wangyl.goldenlife.obj.Global
import net.wangyl.goldenlife.ui.frag.LoginFragment
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
        if (!Global.isLogin()) {
            goSimpleActivity(LoginFragment::class.java)
        } else {
            launchMain()
        }
        finish()
    }
}



