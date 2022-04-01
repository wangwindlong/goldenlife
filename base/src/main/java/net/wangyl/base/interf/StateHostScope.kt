package net.wangyl.base.interf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.enums.LoadingState
import net.wangyl.base.enums.LoginState

public fun CoroutineScope.stateHost(): StateContainer {

   return DefaultContainer(
        this,
        MutableLiveData<LoadingState>(),
        MutableLiveData<ErrorMessage>(),
        MutableLiveData<LoginState>(),
    ).apply {
        //observe at init??
    }
}

val StateHost.loading: LiveData<LoadingState>
    get() = stateContainer.loadingState
val StateHost.errored: LiveData<ErrorMessage>
    get() = stateContainer.errorState
val StateHost.isLogin: LiveData<LoginState>
    get() = stateContainer.loginState


class DefaultContainer(
    val vmScope: CoroutineScope,
    val loading: MutableLiveData<LoadingState>,
    val error: MutableLiveData<ErrorMessage>,
    val login: MutableLiveData<LoginState>
) : StateContainer {
    override val loadingState: MutableLiveData<LoadingState>
        get() = loading
    override val errorState: MutableLiveData<ErrorMessage>
        get() = error
    override val loginState: MutableLiveData<LoginState>
        get() = login

    override fun <T> launchApi(api: CoroutineScope.() -> T) {
        vmScope.api()
    }


}