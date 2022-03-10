package net.wangyl.goldenlife.base

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zackratos.ultimatebarx.ultimatebarx.navigationBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBar
import net.wangyl.goldenlife.R
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

const val TAG_FRAGNAME = "fragment_classname"

open class BaseActivity : AppCompatActivity(), IBase {
    val TAG = javaClass.simpleName
    var fragName: String? = ""
    private var mBackListener: OnBackPressedListener? = null
    private var _delegate: ILifeDelegate? = null

    override fun baseDelegate(): ILifeDelegate? {
        return if (_delegate == null) {
            _delegate = LifeDelegateIml(this)
            _delegate
        } else _delegate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(initLayoutId())
        super.onCreate(savedInstanceState)

        fragName = (savedInstanceState ?: intent.extras)?.getString(TAG_FRAGNAME)
        Timber.d("onCreate fragName= $fragName , savedInstanceState=$savedInstanceState, intent.extras=${intent.extras}")

        findViewById<View>(R.id.fragment_container)?.let { _ ->
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return
            }
            createFragment()?.let {
                // In case this activity was started with special instructions from an Intent,
                // pass the Intent's extras to the fragment as arguments
                it.arguments = intent.extras
                // Add the fragment to the 'fragment_container' FrameLayout
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, it).commitAllowingStateLoss()
            }
        }
        if (fullScreen()) {
            statusBar { transparent() }
            navigationBar { transparent() }
        }
    }

    open fun initLayoutId() : Int {
        return R.layout.activity_base_fragment
    }

    open fun createFragment(): Fragment? {
        return if (fragName?.isNotEmpty() == true) {
            try {
                val frag = supportFragmentManager.fragmentFactory.instantiate(classLoader, fragName!!)
                if (frag is OnBackPressedListener) {
                    mBackListener = frag
                }
                frag
            } catch (e: Exception) {
                null
            }
        } else null
    }

    override fun fullScreen(): Boolean {
        return true
    }

    override fun onBackPressed() {
        if (mBackListener?.doBack() == true) {
            return
        }
        if (supportFragmentManager.backStackEntryCount <= 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBackListener = null
    }

    @Subscribe
    open fun onReceive(data: MsgEvent<Any>) {
        Timber.d("$TAG received data: ${data.from}， ${data.msg}")
    }
}

interface OnBackPressedListener {
    fun doBack() : Boolean
}