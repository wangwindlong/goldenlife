package net.wangyl.life.startup

import android.content.Context
import androidx.startup.Initializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import net.wangyl.base.extension.toast
import net.wangyl.life.obj.Global
import net.wangyl.life.obj.Global.LOGIN
import net.wangyl.life.obj.Global.LOGOUT

class AuthInitializer : Initializer<Unit> {
    lateinit var appScope: CoroutineScope //创建一个全局的协程作用域 监听登录状态

    override fun create(context: Context) {
        appScope = MainScope()
        appScope.launch {
            Global.authFlow.collect {
                if (isLogout(it)) {
                    context.toast("退出登录，清空缓存重启app")
                } else if (isLogin(it)) {
                    context.toast("进入登录页面")
                }
            }
        }
    }

    override fun dependencies() = listOf(
        KoinInitializer::class.java,
        TimberInitializer::class.java,
    )

    companion object {
        fun isLogin(status: Int) = status == LOGIN
        fun isLogout(status: Int) = status == LOGOUT
    }
}