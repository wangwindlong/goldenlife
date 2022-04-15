package net.wangyl.goldenlife.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.onSuccess
import net.wangyl.base.extension.getK
import net.wangyl.base.interf.*
import net.wangyl.goldenlife.api.ApiService
import net.wangyl.goldenlife.api.repo.RSSRepository
import net.wangyl.goldenlife.model.UserSession
import timber.log.Timber

class LoginViewModel(val api: ApiService = getK()) : ViewModel(), StateHost {

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

    override val stateContainer: StateContainer = initDefault()
}