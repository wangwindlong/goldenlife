package net.wangyl.base.interf

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.enums.StateError
import net.wangyl.base.enums.StateIdle
import net.wangyl.base.enums.StateLoading
import net.wangyl.base.http.*
import net.wangyl.base.util.*
import timber.log.Timber
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


/**
 * 用来标记一些状态，加载框、是否登录状态、加载错误信息框等
 */
interface StateHost<T : State> {
    val stateContainer: StateContainer<T>

//    inline fun <reified T : BaseModel> ViewModel.fire(
//        context: CoroutineContext = Dispatchers.IO,
//        handler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//            Timber.e("${javaClass.name} caught the exception ", throwable)
//        },
//        crossinline block: suspend () -> ApiResponse<T>
//    ): LiveData<ApiResponse<T>> = liveData(context + handler) {
//        this.runCatching {
//            emit(ApiResponse.StartResponse())
//            block()
//        }.onSuccess {
//            //status code 为200，继续判断 errorCode 是否为 0
//            emit(
//                when (it.success) {
//                    true -> checkEmptyResponse(it.data)
//                    false -> ApiResponse.ApiError(ErrorMessage(it))
//                }
//            )
//        }.onFailure { throwable ->
//            emit(ApiResponse.ApiError(handleException(throwable)))
//        }
//    }
//
//    /**
//     * data 为 null，或者 data 是集合类型，但是集合为空都会进入 onEmpty 回调
//     */
//    inline fun <reified T> checkEmptyResponse(data: T?): ApiResponse<T> =
//        if (data == null || (data is List<*> && (data as List<*>).isEmpty())) {
//            ApiResponse.ApiEmpty()
//        } else {
//            ApiResponse.ApiSuccess(data, true)
//        }

    companion object {

    }
}

//提供两种方式 简便地初始化默认 stateContainer = initDefault() 或 by StateDelegate()
fun ViewModel.initDefault(initial: State) = viewModelScope.stateHost(initial)

inline fun <reified T : State> stateOf(initial: T): ReadOnlyProperty<ViewModel, StateContainer<T>> {
    return object : ReadOnlyProperty<ViewModel, StateContainer<T>> {
        private var _delegate: StateContainer<T>? = null

        override fun getValue(thisRef: ViewModel, property: KProperty<*>): StateContainer<T> {
            return _delegate ?: thisRef.viewModelScope.stateHost(initial)
        }

    }
}

/**
 * method和参数用来做api缓存
 */
inline fun <reified T> StateHost<State>.apiCall(
    method: String? = null, params: Map<String, Any?> = emptyMap(),
//        handler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//            Timber.e("${javaClass.name} caught the exception ", throwable)
//        },
    showDialog: Boolean = true, useCache: Boolean = true,
    crossinline block: suspend (Map<String, Any?>) -> ApiResponse<T>
) = flow {
    hasCache<T>(method, params, useCache) {
        emit(ApiResponse.ApiSuccess(it, true))
        return@flow
    }.noCache {
//        stateContainer.loadingState.value = if (showDialog) StateLoading else StateIdle
        try {
            emit(BaseRepository.apiCall {
                block(params)
            }.also {
                Timber.d("emit apicall result = $it")
                if (it !is ApiResponse.ApiSuccess || !useCache) return@also
                Cache.saveCache(this, it.data)
            })
//            stateContainer.launchApi {
//
//            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
//        stateContainer.loadingState.postValue(StateFinished)
    }
}

//类型别名
typealias LaunchBlock = suspend CoroutineScope.() -> Unit

typealias EmitBlock<T> = suspend LiveDataScope<T>.() -> T

typealias Cancel = (e: Exception) -> Unit

//        handler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//            Timber.e("${javaClass.name} caught the exception ", throwable)
//        }


inline fun StateHost<*>.launch(
    scope: CoroutineScope,
    showDialog: Boolean = true,
    showError: Boolean = false,
    noinline cancel: Cancel? = null,
    crossinline block: LaunchBlock
) {
    scope.launch {
        stateContainer.updateLoading(if (showDialog) StateLoading else StateIdle)
        try {
            block()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            cancel?.invoke(e)
            if (showError) stateContainer.updateLoading(StateError(e))
        }
        stateContainer.updateLoading(StateIdle)
    }

}

inline fun <reified T> StateHost<*>.api(
    showDialog: Boolean = true,
    showError: Boolean = false,
    noinline cancel: Cancel? = null,
    crossinline block: EmitBlock<T?>
): LiveData<T?> = liveData {
    stateContainer.updateLoading(if (showDialog) StateLoading else StateIdle)
    try {
        emit(block())
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        cancel?.invoke(e)
        if (showError) stateContainer.updateLoading(StateError(e))
    }
    stateContainer.updateLoading(StateIdle)
}

//fun StateHost.api(
//    method: String? = null, params: Map<String, Any?> = emptyMap(),
////        handler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
////            Timber.e("${javaClass.name} caught the exception ", throwable)
////        },
//    showDialog: Boolean = true,
//    block: (Map<String, Any?>) -> Unit
//) {
//    stateContainer.launchApi {
//        block(params)
//    }
////        stateContainer.launchApi {
////            block(params) }
////    BaseRepository.apiCall {  }
//}

inline fun <T> Flow<T>.safeCollect(
    lifecycleOwner: LifecycleOwner,
    crossinline callback: (T) -> Unit
) {
    lifecycleOwner.lifecycleScope.launch {
        // See https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            collect {
                callback(it)
            }
        }
    }
}

suspend inline fun callOnMain(crossinline block: () -> Unit) {
    withContext(Dispatchers.Main) {
        Timber.d("callOnMain threadid=${Thread.currentThread().id}")
        block()
    }
}



