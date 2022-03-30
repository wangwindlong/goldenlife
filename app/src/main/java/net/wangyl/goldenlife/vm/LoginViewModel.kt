package net.wangyl.goldenlife.vm

import androidx.lifecycle.ViewModel
import net.wangyl.base.extension.getK
import net.wangyl.base.interf.StateContainer
import net.wangyl.base.interf.StateHost
import net.wangyl.base.interf.apiCall
import net.wangyl.base.interf.initDefault
import net.wangyl.goldenlife.api.ApiService
import net.wangyl.goldenlife.api.repo.RSSRepository

class LoginViewModel: ViewModel(), StateHost {

    fun login() {
        apiCall(getK<RSSRepository>()) {
            val stringMap = HashMap<String, String>()
            stringMap["op"] = "login"
            stringMap["user"] = "admin"
            stringMap["password"] = "6629782"
            getK<ApiService>().login(stringMap)
        }
    }

    override val stateContainer: StateContainer
        get() = initDefault()
}