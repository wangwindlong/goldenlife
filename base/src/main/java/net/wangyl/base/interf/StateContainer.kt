package net.wangyl.base.interf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.enums.StateEvent

/**
 * 监听通用状态的类，方便ui层按需展示
 */
interface StateContainer<T : State>: LoadingState {
    val stateFlow: StateFlow<T>  //返回的数据
    val settings: Settings

    data class Settings(
        val sideEffectBufferSize: Int = Channel.UNLIMITED,
        val showLoading: Boolean = true,
        val showError: Boolean = true,
        val exceptionHandler: CoroutineExceptionHandler? = null,
        val apiDelay: Long = 150L,
        val maxAttempts: Int = 3,
    )

    fun launchApi(api: suspend CoroutineScope.() -> T)
}

interface LoadingState {
    val loadingState : Flow<StateEvent>  //加载状态
//    val loadingState: MutableLiveData<StateEvent>  //加载状态
//    val errorState : MutableLiveData<ErrorMessage>   //错误码 ErrorMessage
    fun updateLoading(event: StateEvent) {

    }

}

interface State {

}



