package net.wangyl.base.interf

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.enums.*
import org.orbitmvi.orbit.Container
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.EmptyCoroutineContext

//默认的viewmodel scope实现
fun <T : State> CoroutineScope.stateHost(
    initial: T, settings: StateContainer.Settings = StateContainer.Settings()
): StateContainer<T> {

    return DefaultContainer(this, initial, settings).apply {
        //observe at init??

    }
}

fun StateHost<*>.collectError() {

}

val <T : State> StateHost<T>.dataFlow: StateFlow<T>
    get() = stateContainer.stateFlow

val StateHost<*>.loadingFlow: Flow<StateEvent>
    get() = stateContainer.loadingState
val StateHost<*>.loading: LiveData<StateEvent>
    get() = stateContainer.loadingState.asLiveData()
//val StateHost<*>.errored: LiveData<ErrorMessage>
//    get() = stateContainer.errorState

//inline fun <reified T> StateContainer<*>.setState(update: (old: T) -> T): T = _state.updateAndGet(update)

class DefaultContainer<T : State>(
    val vmScope: CoroutineScope,
    initial: T, override val settings: StateContainer.Settings
) : StateContainer<T> {
    private val _state = MutableStateFlow(initial)
    //记录上一次的加载状态 并增加一个额外的缓存
    private val _loadingState = MutableSharedFlow<StateEvent>(replay = 1, extraBufferCapacity = 1)
    val error = MutableLiveData<ErrorMessage>()
    private val mutex = Mutex()

    override val stateFlow: StateFlow<T>
        get() = _state.asStateFlow()

    override val loadingState: Flow<StateEvent>
        get() = _loadingState
//    override val errorState: LiveData<ErrorMessage>
//        get() = error

    override fun launchApi(api: suspend CoroutineScope.() -> T) {
        _loadingState.tryEmit(if (settings.showLoading) StateLoading else StateIdle)
        vmScope.launch(settings.exceptionHandler ?: EmptyCoroutineContext) {
            withRetry(settings.apiDelay, settings.maxAttempts) {
                supervisorScope {
                    mutex.withLock {
                        try {
                            _state.value = vmScope.api()
                        } catch (e: Exception) {
                            _loadingState.tryEmit(StateError(e))
                        }
                    }
                }
            }
        }
        _loadingState.tryEmit(StateIdle)
    }

    override fun updateLoading(event: StateEvent) {
        _loadingState.tryEmit(event)
    }
}

suspend fun <T> withRetry(
    defaultDelay: Long = 100,
    maxAttempts: Int = 3,
    shouldRetry: (Throwable) -> Boolean = ::defaultShouldRetry,
    block: suspend () -> T
): T {
    repeat(maxAttempts) { attempt ->
        delay(if (attempt == 0) defaultDelay else 0)
        val response = runCatching { block() }

        when {
            response.isSuccess -> return response.getOrThrow()
            response.isFailure -> {
                val exception = response.exceptionOrNull()!!

                // The response failed, so lets see if we should retry again
                if (attempt == maxAttempts - 1 || !shouldRetry(exception)) {
                    throw exception
                }

                var nextDelay = attempt * attempt * defaultDelay

                if (exception is HttpException) {
                    // If we have a HttpException, check whether we have a Retry-After
                    // header to decide how long to delay
                    exception.retryAfter?.let {
                        nextDelay = it.coerceAtLeast(defaultDelay)
                    }
                }
                delay(nextDelay)
            }
        }
    }

    // We should never hit here
    throw IllegalStateException("Unknown exception from executeWithRetry")
}

private val HttpException.retryAfter: Long?
    get() {
        val retryAfterHeader = response()?.headers()?.get("Retry-After")
        if (retryAfterHeader != null && retryAfterHeader.isNotEmpty()) {
            // Got a Retry-After value, try and parse it to an long
            try {
                return retryAfterHeader.toLong() + 10
            } catch (nfe: NumberFormatException) {
                // Probably won't happen, ignore the value and use the generated default above
            }
        }
        return null
    }

private fun defaultShouldRetry(throwable: Throwable) = when (throwable) {
    is HttpException -> throwable.code() == 429
    is IOException -> true
    else -> false
}