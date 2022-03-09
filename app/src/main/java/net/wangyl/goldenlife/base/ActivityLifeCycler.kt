package net.wangyl.goldenlife.base

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import net.wangyl.goldenlife.BuildConfig
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.extension.getK
import net.wangyl.goldenlife.utils.manager.AppManager
import timber.log.Timber


class ActivityLifeCycler private constructor() : ActivityLifecycleCallbacks {

    companion object {
        val instance = ActivityLifeCycler.holder
    }

    private object ActivityLifeCycler {
        val holder = ActivityLifeCycler()
    }

    init {
        if (BuildConfig.DEBUG) {
            //Timber初始化
            //Timber 是一个日志框架容器,外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略)进行日志打印
            //并且支持添加多个日志框架(打印策略),做到外部调用一次 Api,内部却可以做到同时使用多个策略
            //比如添加三个策略,一个打印日志,一个将日志保存本地,一个将日志上传服务器
            Timber.plant(Timber.DebugTree())
            // 如果你想将框架切换为 Logger 来打印日志,请使用下面的代码,如想切换为其他日志框架请根据下面的方式扩展
//                    Logger.addLogAdapter(new AndroidLogAdapter());
//                    Timber.plant(new Timber.DebugTree() {
//                        @Override
//                        protected void log(int priority, String tag, String message, Throwable t) {
//                            Logger.log(priority, tag, message, t);
//                        }
//                    });
        }
    }


    override fun onActivityCreated(f: Activity, savedInstanceState: Bundle?) {
        AppManager.get().pushActivity(f)
        //mFragmentLifecycle 为 Fragment 生命周期实现类, 用于框架内部对每个 Fragment 的必要操作, 如给每个 Fragment 配置 FragmentDelegate
        //注册框架内部已实现的 Fragment 生命周期逻辑
        (f as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
            getK(), true)
        (f as? IBase)?.getDelegate()?.apply {
            onCreate(savedInstanceState)
            if (f.fullScreen()) {
//                statusBar { transparent() }
//                navigationBar { transparent() }
            }
        }
        if (f is AppCompatActivity && f.findViewById<View>(R.id.toolbar) != null) {
            f.findViewById<Toolbar>(R.id.toolbar)?.let {
                f.setSupportActionBar(it)
                f.supportActionBar?.setDisplayShowTitleEnabled(true)
                //初始化其他事件，返回，actionbar等
            }
        }
    }

    override fun onActivityStarted(f: Activity) {
        (f as? IBase)?.getDelegate()?.onStart()
        Timber.d("onActivityStarted")
    }

    override fun onActivityResumed(f: Activity) {
        AppManager.get().setCurrentActivity(f)
        (f as? IBase)?.getDelegate()?.onResume()
        Timber.d("onActivityResumed")
    }

    override fun onActivityPaused(f: Activity) {
        (f as? IBase)?.getDelegate()?.onPause()
        Timber.d("onActivityPaused")
    }

    override fun onActivityStopped(f: Activity) {
        if (AppManager.get().getCurrentActivity() === f) {
            AppManager.get().setCurrentActivity(null)
        }
        (f as? IBase)?.getDelegate()?.onStop()
        Timber.d("onActivityStopped")
    }

    override fun onActivitySaveInstanceState(f: Activity, outState: Bundle) {
        (f as? IBase)?.getDelegate()?.onSaveInstanceState(outState)
        Timber.d("onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(f: Activity) {
        AppManager.get().popActivity(f)
        (f as? IBase)?.getDelegate()?.onDetach()
        Timber.d("onActivityDestroyed ")
    }
}