package net.wangyl.goldenlife.obj

import android.content.Intent
import com.google.gson.Gson
import net.wangyl.base.extension.getK
import net.wangyl.base.manager.AppManager
import net.wangyl.eventbus_flow.core.postEvent
import net.wangyl.goldenlife.Constants
import net.wangyl.goldenlife.GoldApp
import net.wangyl.goldenlife.event.GlobalEvent
import net.wangyl.goldenlife.model.UserSession
import net.wangyl.goldenlife.ui.LaunchActivity
import timber.log.Timber

object Global {
    const val UN_LOGIN = "{\"session_id\": null}"
    const val TAG_USER = "rss_user_tag"

    var userSession: UserSession = getK<Gson>().fromJson(
        PrefManager.get().getString(TAG_USER, UN_LOGIN),
        UserSession::class.java
    )
        set(value) {
            if (field == value) return
            field = value
            PrefManager.get().putString(TAG_USER, getK<Gson>().toJson(value))
        }

    fun isLogin() = userSession.session_id?.isNotEmpty() == true

    fun clearUserData() {
        userSession = UserSession()
    }

    val sessionId: String?
        get() = userSession.session_id


    fun logOut() {
        clearUserData()
        postEvent(GlobalEvent(Constants.EVENT_LOGOUT))
    }


    fun relaunchApp() {
        AppManager.get().clearAllActivity()
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
            setClassName(GoldApp.sInstance.packageName, LaunchActivity::class.java.name)
        }
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        GoldApp.sInstance.startActivity(intent)
    }
}