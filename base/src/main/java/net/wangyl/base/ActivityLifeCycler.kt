package net.wangyl.base

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import net.wangyl.base.base.IBase
import net.wangyl.base.dialog.LoadingDialog
import net.wangyl.base.enums.StateError
import net.wangyl.base.enums.StateLoading
import net.wangyl.base.extension.getK
import net.wangyl.base.interf.loading
import net.wangyl.base.manager.AppManager
import timber.log.Timber
import java.util.*


class ActivityLifeCycler private constructor() : ActivityLifecycleCallbacks {
    //埋点信息，参考 https://juejin.cn/post/6844903734686777357
    // https://github.com/Liberuman/Tracker
    private val durationMap = WeakHashMap<Context, Long>()
    private val resumeTimeMap = WeakHashMap<Context, Long>()
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

    override fun onActivityPreCreated(f: Activity, savedInstanceState: Bundle?) {
//        Timber.d("onActivityPreCreated $f , savedInstanceState= ${savedInstanceState}")
//        Bridge.restoreInstanceState(f, savedInstanceState)

    }

    override fun onActivityCreated(f: Activity, savedInstanceState: Bundle?) {
        durationMap[f] = 0L
        Timber.d("onActivityCreated $f")
        AppManager.get().pushActivity(f)
        //mFragmentLifecycle 为 Fragment 生命周期实现类, 用于框架内部对每个 Fragment 的必要操作, 如给每个 Fragment 配置 FragmentDelegate
        //注册框架内部已实现的 Fragment 生命周期逻辑
        (f as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(getK(), true)
        (f as? IBase)?.baseDelegate?.onCreate(savedInstanceState)
        //使用https://github.com/livefront/bridge 加载保存的状态
    }

    override fun onActivityStarted(f: Activity) {
        val toolbar: Toolbar? = f.findViewById(R.id.base_toolbar)
        (f as? IBase)?.let {
            it.baseDelegate?.onStart()
            if (f.showAction) {
                f.setupToolbar(toolbar)
                toolbar?.let { tool ->
                    (f as? AppCompatActivity)?.setSupportActionBar(tool)
                    (f as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(true)
//                it.setTitle(f.title)
                    //初始化其他事件，返回，actionbar等
                    tool.setNavigationOnClickListener {
                        f.onBackPressed()
                    }
                }
            }
        }

        Timber.d("onActivityStarted")
    }

    override fun onActivityResumed(f: Activity) {
        AppManager.get().setCurrentActivity(f)
        (f as? IBase)?.baseDelegate?.onResume()
        Timber.d("onActivityResumed")
        resumeTimeMap[f] = System.currentTimeMillis()
    }

    override fun onActivityPaused(f: Activity) {
        (f as? IBase)?.baseDelegate?.onPause()
        Timber.d("onActivityPaused")
        durationMap[f] = (durationMap[f]!! + (System.currentTimeMillis() - resumeTimeMap[f]!!))
    }

    override fun onActivityStopped(f: Activity) {
        if (AppManager.get().getCurrentActivity() === f) {
            AppManager.get().setCurrentActivity(null)
        }
        (f as? IBase)?.baseDelegate?.onStop()
        Timber.d("onActivityStopped")
    }



    override fun onActivitySaveInstanceState(f: Activity, outState: Bundle) {
        (f as? IBase)?.baseDelegate?.onSaveInstanceState(outState)
        Timber.d("onActivitySaveInstanceState")
//        Bridge.saveInstanceState(f, outState)
    }

    override fun onActivityDestroyed(f: Activity) {
        AppManager.get().popActivity(f)
        (f as? IBase)?.baseDelegate?.onDestroy()
        Timber.d("onActivityDestroyed ")
        durationMap[f]?.let {
            // 将事件添加到数据库

            durationMap.remove(f)

        }
        resumeTimeMap.remove(f)
    }
}