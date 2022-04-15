package net.wangyl.base.interf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.enums.LoginState
import net.wangyl.base.enums.StateEvent

fun CoroutineScope.stateHost(): StateContainer {

   return DefaultContainer(
        this,
        MutableLiveData<StateEvent>(),
        MutableLiveData<ErrorMessage>(),
        MutableLiveData<Any>(),
    ).apply {
        //observe at init??
    }
}

val StateHost.loading: LiveData<StateEvent>
    get() = stateContainer.loadingState
val StateHost.errored: LiveData<ErrorMessage>
    get() = stateContainer.errorState
val StateHost.dataState: LiveData<Any>
    get() = stateContainer.dataState


class DefaultContainer(
    val vmScope: CoroutineScope,
    val loading: MutableLiveData<StateEvent>,
    val error: MutableLiveData<ErrorMessage>,
    val login: MutableLiveData<Any>
) : StateContainer {
    override val loadingState: MutableLiveData<StateEvent>
        get() = loading
    override val errorState: MutableLiveData<ErrorMessage>
        get() = error
    override val dataState: MutableLiveData<Any>
        get() = login

    override fun <T> launchApi(api: CoroutineScope.() -> T) {
        vmScope.api()
    }


}