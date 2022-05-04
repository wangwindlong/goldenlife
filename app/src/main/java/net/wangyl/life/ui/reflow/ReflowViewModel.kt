package net.wangyl.life.ui.reflow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.wangyl.base.interf.*
import net.wangyl.life.compose.state.DefaultState

class ReflowViewModel : ViewModel(), StateHost<DefaultState> {

    override val stateContainer by stateOf(DefaultState)

    private val _text = MutableLiveData<String>().apply {
        value = "This is reflow Fragment"
    }
    val text: LiveData<String> = _text

    private val _loginState = MutableStateFlow<LoginUIState>(LoginUIState.Empty)
    val loginUIState: StateFlow<LoginUIState> = _loginState

    // simulate login process
    fun login(username: String, password: String) = viewModelScope.launch {
        _loginState.value = LoginUIState.Loading
        // fake network request time
        delay(2000L)
        if (username == "raheem" && password == "android") {
            _loginState.value = LoginUIState.Success
        } else {
            _loginState.value = LoginUIState.Error("Incorrect password")
        }
    }

    // login ui states
    sealed class LoginUIState {
        object Success : LoginUIState()
        data class Error(val message: String) : LoginUIState()
        object Loading : LoginUIState()
        object Empty : LoginUIState()
    }


}