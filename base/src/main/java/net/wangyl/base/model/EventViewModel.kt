package net.wangyl.base.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @function 全局 ViewModel，用于处理全局消息
 */
class EventViewModel : ViewModel() {
    val loadingLiveData = MutableLiveData<Boolean>()

    val toastLiveData = MutableLiveData<String?>()

    fun showLoading() {
        loadingLiveData.value = true
    }

    fun dismissLoading() {
        loadingLiveData.value = false
    }

    fun showToast(msg: String?) {
        toastLiveData.value = msg
    }



}