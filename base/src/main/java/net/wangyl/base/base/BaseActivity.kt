package net.wangyl.base.base

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import icepick.State
import net.wangyl.base.Configs.APP_NAME
import net.wangyl.base.R
import net.wangyl.base.data.MsgEvent
import net.wangyl.base.widget.toolbar.LoadingImpl
import net.wangyl.base.widget.toolbar.LoadingState
import net.wangyl.base.widget.toolbar.NavBtnType
import net.wangyl.base.widget.toolbar.setToolbar
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

const val TAG_FRAGNAME = "fragment_classname"
const val TAG_DECORATE = "fragment_decorate"
const val TAG_ARGS = "fragment_arguments"
const val TAG_TITLE = "tag_title"

open class BaseActivity : AppCompatActivity(), IBase by BaseImpl(), LoadingState by LoadingImpl() {
    val TAG = javaClass.simpleName
    @State
    @JvmField var fragName: String? = ""
    private var mBackListener: OnBackPressedListener? = null
    private var mToolbar: Toolbar? = null
    @State
    @JvmField var mArgs: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras ?: savedInstanceState
        fragName = intent.extras?.getString(TAG_FRAGNAME)
        isDecorated = intent.extras?.getBoolean(TAG_DECORATE) ?: false
        mArgs = intent.getBundleExtra(TAG_ARGS)
        Timber.d("onCreate fragName= $fragName, intent.extras=${intent.extras} , " +
                    "isDecorated = $isDecorated, mArgs=${mArgs?.getInt(TAG_TAB)}, intent.extras=${intent.extras?.getBundle(TAG_ARGS)} savedInstanceState=$savedInstanceState")
        setContentView(initLayoutId())

        findViewById<View>(R.id.fragment_container)?.let { _ ->
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return
            }
            createFragment()?.let {
                //将当前activity的所有参数传给fragment
//                it.arguments = intent.extras
                //将特定fragment的参数传递给fragment
                it.arguments = mArgs
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, it)
//                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }
        mToolbar = findViewById(R.id.base_toolbar)
        if (isDecorated)
            setToolbar(title ?: APP_NAME, NavBtnType.ICON_TEXT) {
                navClickListener = View.OnClickListener { onBackPressed() }
            }
//        if (fullScreen()) {
//            statusBar { transparent() }
//            navigationBar { transparent() }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.d("onOptionsItemSelected item=$item")
        when (item.getItemId()) {
            android.R.id.home -> {
                //do something here like
                val backStackEntryCount = supportFragmentManager.backStackEntryCount
                if (backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                }
                return true
            }
        }
        return false
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        decorateContentView(isDecorated)
    }

    open fun initLayoutId(): Int {
        return R.layout.base_activity_base_fragment
    }

    open fun createFragment(): Fragment? {
        return if (fragName != null) {
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

    override fun setupToolbar(toolbar: Toolbar?) {
        toolbar?.let {
            mToolbar = it
//            it.visibility = View.GONE
//            UltimateBarX.statusBarOnly(this)
//                .transparent()
//                .fitWindow(false)
//                .light(true)
//                .apply()
        }
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
    fun doBack(): Boolean
}