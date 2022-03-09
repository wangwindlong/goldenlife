package net.wangyl.goldenlife.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.zackratos.ultimatebarx.ultimatebarx.navigationBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBar
import net.wangyl.goldenlife.R
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

class BaseActivity: FragmentActivity(), IBase {
    val TAG = javaClass.simpleName
    private var _delegate: ILifeDelegate? = null

    override fun getDelegate(): ILifeDelegate? {
        return if (_delegate == null) {
            _delegate = LifeDelegateIml(this)
            _delegate
        } else _delegate
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.activity_base_fragment)

        if (findViewById<View>(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return
            }
//            contentFragment = createFragment()
//            if (contentFragment == null) return
//            // In case this activity was started with special instructions from an Intent,
//            // pass the Intent's extras to the fragment as arguments
//            contentFragment!!.arguments = intent.extras
//
//            // Add the fragment to the 'fragment_container' FrameLayout
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragment_container, contentFragment!!).commitAllowingStateLoss()
        }
        if (fullScreen()) {
            statusBar { transparent() }
            navigationBar { transparent() }
        }
    }

    override fun fullScreen(): Boolean {
        return true
    }

    @Subscribe
    open fun onReceive(data: MsgEvent<Any>) {
        Timber.d("$TAG received data: ${data.from}， ${data.msg}")
    }
}