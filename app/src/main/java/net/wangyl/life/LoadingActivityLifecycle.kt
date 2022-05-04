package net.wangyl.life

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 加载全局dialog
 */
class LoadingActivityLifecycle : ActivityLifecycleCallbacks {
    private fun initLoadingObserver(activity: AppCompatActivity) {
//        getK<EventViewModel>().loadingLiveData.observe(activity) {
//            if (it == true) {
//                LoadingDialog.show(activity)
//            } else {
//                LoadingDialog.dismiss(activity)
//            }
//        }
    }

    private fun initToastObserver(activity: AppCompatActivity) {
//        getK<EventViewModel>().toastLiveData.observe(activity) { msg ->
//            msg?.takeIf { it.isNotEmpty() }?.also {
//                activity.toast(it)
//            }
//        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        (activity as? AppCompatActivity)?.let {
            initLoadingObserver(it)
            initToastObserver(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}