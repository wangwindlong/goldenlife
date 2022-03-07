package net.wangyl.goldenlife.utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*


class ActivityLifeCycler private constructor() : ActivityLifecycleCallbacks {

    companion object {
        val instance = ActivityLifeCycler.holder
    }

    private object ActivityLifeCycler {
        val holder= ActivityLifeCycler()
    }



    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

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