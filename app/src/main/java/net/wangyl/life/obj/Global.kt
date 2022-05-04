package net.wangyl.life.obj

import android.content.Intent
import android.os.Debug
import androidx.compose.runtime.*
import androidx.room.Room
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import net.wangyl.base.extension.getK
import net.wangyl.base.manager.AppManager
import net.wangyl.base.model.EventViewModel
import net.wangyl.base.util.fromJson
import net.wangyl.base.util.json
import net.wangyl.eventbus_flow.core.observeEvent
import net.wangyl.eventbus_flow.core.postEvent
import net.wangyl.life.Constants
import net.wangyl.life.GoldApp
import net.wangyl.life.compose.MyComposeTheme.Theme
import net.wangyl.life.data.RSSDatabase
import net.wangyl.life.event.GlobalEvent
import net.wangyl.life.model.UserSession
import net.wangyl.life.obj.Global.TAG_THEME
import net.wangyl.life.ui.LaunchActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Global {
    const val UN_LOGIN = "{\"session_id\": null}"
    const val TAG_USER = "rss_user_tag"
    const val TAG_THEME = "rss_theme_tag"
    const val DB_NAME = "gold.db"
    const val LOGOUT = -1
    const val LOGIN = 1

//    val globalVm = getK<EventViewModel>()
    var userSession = fromJson<UserSession>(PrefManager.get().getString(TAG_USER, UN_LOGIN) ?: UN_LOGIN)
//    var userSession: UserSession = getK<Gson>().fromJson(
//        PrefManager.get().getString(TAG_USER, UN_LOGIN),
//        UserSession::class.java
//    )
        set(value) {
            if (field == value) return
            field = value
            PrefManager.get().putString(TAG_USER, value.json)
        }

    fun isLogin() = userSession?.session_id?.isNotEmpty() == true

    fun clearUserData() {
        userSession = UserSession()
    }

    val sessionId: String?
        get() = userSession?.session_id

    var theme by ThemeDelegate()
    val authFlow = MutableSharedFlow<Int>(replay = 1)

    fun logIn() {
        authFlow.tryEmit(LOGIN)
    }

    fun logOut() {
        clearUserData()
        //使用flow eventbus 发送信息，需要在有声明周期的组件内注册 observeEvent 监听事件 略麻烦
//        postEvent(GlobalEvent(Constants.EVENT_LOGOUT))
        //使用全局flow, 发送到AuthInitializer中的协程
        authFlow.tryEmit(LOGOUT)
//        relaunchApp()
    }

    //重新打开app
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

class ThemeDelegate: ReadWriteProperty<Any, Theme> {
    var _state = mutableStateOf(Theme.fromInt(PrefManager.get().getInt(TAG_THEME, 0)))

    override fun getValue(thisRef: Any, property: KProperty<*>): Theme {
        return _state.value
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Theme) {
        if (value != _state.value) {
            _state.value = value
            PrefManager.get().putInt(TAG_THEME, value.state)
        }
    }

}


