package net.wangyl.goldenlife.utils

import androidx.fragment.app.FragmentManager

class CommonUtils {


    companion object {
        @JvmStatic
        var DEPENDENCY_AUTO_LAYOUT = findClassByClassName("com.zhy.autolayout.AutoLayoutInfo")
        @JvmStatic
        var DEPENDENCY_SUPPORT_DESIGN = findClassByClassName("com.google.android.material.snackbar.Snackbar")
        @JvmStatic
        var DEPENDENCY_GLIDE = findClassByClassName("com.bumptech.glide.Glide")
        @JvmStatic
        var DEPENDENCY_ANDROID_EVENTBUS = findClassByClassName("org.simple.eventbus.EventBus")
        @JvmStatic
        var DEPENDENCY_EVENTBUS = findClassByClassName("org.greenrobot.eventbus.EventBus")

    }


}


fun findClassByClassName(className: String): Boolean {
    val hasDependency: Boolean
    hasDependency = try {
        Class.forName(className)
        true
    } catch (e: ClassNotFoundException) {
        false
    }
    return hasDependency
}