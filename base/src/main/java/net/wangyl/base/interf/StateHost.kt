package net.wangyl.base.interf

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.enums.LoadingState
import net.wangyl.base.enums.LoginState
import net.wangyl.base.extension.getK
import net.wangyl.base.http.*
import timber.log.Timber


/**
 * 用来标记一些状态，加载框、是否登录状态、加载错误信息框等
 */
interface StateHost {
    val stateContainer: StateContainer

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

fun ViewModel.initDefault() = DefaultContainer(
    MutableLiveData<LoadingState>(),
    MutableLiveData<ErrorMessage>(),
    MutableLiveData<LoginState>(),
).apply {
    //observe at init??
}

val StateHost.loading: LiveData<LoadingState>
    get() = stateContainer.loadingState
val StateHost.errored: LiveData<ErrorMessage>
    get() = stateContainer.errorState
val StateHost.isLogin : LiveData<LoginState>
    get() = stateContainer.loginState

class DefaultContainer(val loading: MutableLiveData<LoadingState>,
                                val error: MutableLiveData<ErrorMessage>,
                                val login: MutableLiveData<LoginState>
) : StateContainer {
    override val loadingState: MutableLiveData<LoadingState>
        get() = loading
    override val errorState: MutableLiveData<ErrorMessage>
        get() = error
    override val loginState: MutableLiveData<LoginState>
        get() = login

}

/**
 * method用来做缓存
 */
inline fun <reified T> StateHost.apiCall(
    repository: BaseRepository,
    method: String? = null, params: Map<String, Any?> = emptyMap(),
//        handler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
//            Timber.e("${javaClass.name} caught the exception ", throwable)
//        },
    showDialog: Boolean = true,
    crossinline block: suspend BaseRepository.(Map<String, Any?>) -> ApiResponse<T>
) = flow {
    stateContainer.loadingState.value = if (showDialog) LoadingState.LOADING else LoadingState.IDLE
    //因为retrofit支持异步切换
    emit(repository.apiCall {  block(params) })
    stateContainer.loadingState.postValue(LoadingState.FINISHED)
}.onCompletion {
    Timber.d("apiCall onCompletion threadid=${Thread.currentThread().id}")
//    emit(xxx)
//    stateContainer.loadingState.postValue(LoadingState.FINISHED)
}.flowOn(Dispatchers.Main)


suspend inline fun callOnMain(crossinline block: () -> Unit) {
    withContext(Dispatchers.Main) {
        Timber.d("callOnMain threadid=${Thread.currentThread().id}")
        block()
    }
}

