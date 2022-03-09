package net.wangyl.goldenlife.utils.manager

import android.app.Activity
import timber.log.Timber
import java.util.*

class AppManager private constructor() {
    companion object {
        private var instance: AppManager? = null
            get() {
                if (field == null) {
                    field = AppManager()
                }
                return field
            }

        @Synchronized
        fun get(): AppManager{
            return instance!!
        }
    }

    /**
     * 管理所有存活的 Activity, 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     */
    private val mActivitys = Collections.synchronizedList(LinkedList<Activity>())

    /**
     * 当前在前台的 Activity
     */
    private var mCurrentActivity: Activity? = null

    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    fun pushActivity(activity: Activity) {
        if(mActivitys.contains(activity))mActivitys.add(activity)
//        LOGD(TAG, "activityList:size:" + mActivitys.size)
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    fun popActivity(activity: Activity) {
        if (mActivitys.contains(activity)) {
            mActivitys.remove(activity)
        }
//        LOGD(TAG, "activityList:size:" + mActivitys.size)
    }

    fun getCurrentActivity(): Activity? {
        return mCurrentActivity
    }

    fun setCurrentActivity(currentActivity: Activity?) {
        mCurrentActivity = currentActivity
    }

    fun getTopActivity(): Activity? {
        if (mActivitys == null) {
            Timber.w("mActivityList == null when getTopActivity()")
            return null
        }
        return if (mActivitys.size > 0) mActivitys.get(mActivitys.size - 1) else null
    }
}