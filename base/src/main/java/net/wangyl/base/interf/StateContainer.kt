package net.wangyl.base.interf

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.enums.LoginState
import net.wangyl.base.enums.StateEvent

/**
 * 监听通用状态的类，方便ui层按需展示
 * 切换为flow？
 */
interface StateContainer {
    val loadingState: MutableLiveData<StateEvent>  //加载状态
    val errorState : MutableLiveData<ErrorMessage>   //错误码 ErrorMessage
    val dataState : MutableLiveData<Any>   //返回的数据
    fun<T> launchApi(api: CoroutineScope.() -> T)
}