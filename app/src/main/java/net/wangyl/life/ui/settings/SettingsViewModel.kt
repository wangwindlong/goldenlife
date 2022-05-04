package net.wangyl.life.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.wangyl.base.interf.StateHost
import net.wangyl.base.interf.stateOf
import net.wangyl.life.compose.state.DefaultState
import net.wangyl.life.obj.Global.logOut

class SettingsViewModel : ViewModel(), StateHost<DefaultState> {

    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }
    val text: LiveData<String> = _text
    override val stateContainer by stateOf(DefaultState)

    fun logout() {
        viewModelScope.launch {
            delay(1000)
            _text.value = "正在退出..."
            delay(1000)
            logOut()
        }
    }

}