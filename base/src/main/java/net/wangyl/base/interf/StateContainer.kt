package net.wangyl.base.interf

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.enums.LoadingState
import net.wangyl.base.enums.LoginState

/**
 * 监听通用状态的类，方便ui层按需展示
 * 切换为flow？
 */
interface StateContainer {
    val loadingState: MutableLiveData<LoadingState>  //加载状态
    val errorState : MutableLiveData<ErrorMessage>   //错误码 ErrorMessage
    val loginState : MutableLiveData<LoginState>   //是否登录状态
    fun<T> launchApi(api: CoroutineScope.() -> T)
}