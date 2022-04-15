package net.wangyl.goldenlife.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.wangyl.base.interf.StateContainer
import net.wangyl.base.interf.StateDelegate
import net.wangyl.base.interf.StateHost
import net.wangyl.goldenlife.obj.Global.logOut
import org.orbitmvi.orbit.viewmodel.container

class SettingsViewModel : ViewModel(), StateHost {

    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }
    val text: LiveData<String> = _text
    override val stateContainer by StateDelegate()

    fun logout() {
        viewModelScope.launch {
            delay(1000)
            _text.value = "正在退出..."
            delay(1000)
            logOut()
        }
    }

}