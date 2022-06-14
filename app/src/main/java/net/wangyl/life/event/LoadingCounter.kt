package net.wangyl.life.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.enums.StateError
import net.wangyl.base.enums.StateEvent
import net.wangyl.base.enums.StateIdle
import net.wangyl.base.enums.StateLoading
import java.util.concurrent.atomic.AtomicInteger

//加载状态计数器，每加一个loading count++ 当所有加载结束时才停止
class LoadingCounter {
    private val count = AtomicInteger()
    private val loadingState = MutableStateFlow(count.get())

    val observable: Flow<Boolean>
        get() = loadingState.map { it > 0 }.distinctUntilChanged()

    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }

}


suspend fun Flow<StateEvent>.collectStatus(
    counter: LoadingCounter,
//    uiMessageManager: UiMessageManager? = null,
) = collect { status ->
    when (status) {
        StateLoading -> counter.addLoader()
        StateIdle -> counter.removeLoader()
        is StateError -> {
//            uiMessageManager?.emitMessage(ApiResponse.ApiError(status.throwable))
            counter.removeLoader()
        }
    }
}
