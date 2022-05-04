package net.wangyl.life.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import net.wangyl.base.data.onSuccess
import net.wangyl.base.extension.getK
import net.wangyl.base.interf.*
import net.wangyl.life.api.ApiService
import net.wangyl.life.compose.state.DefaultState
import net.wangyl.life.model.UserSession

class LoginViewModel(val api: ApiService = getK()) : ViewModel(), StateHost<DefaultState> {

    fun login(handler: (UserSession) -> Unit) {
        val stringMap = HashMap<String, String>()
        stringMap["op"] = "login"
        stringMap["user"] = "admin"
        stringMap["password"] = "6629782"
        launch(viewModelScope) {
            api.login(stringMap).onSuccess {
                handler(this.data)
            }
        }
    }

//    override val stateContainer = initDefault(DefaultState)
    override val stateContainer by stateOf(DefaultState)
}