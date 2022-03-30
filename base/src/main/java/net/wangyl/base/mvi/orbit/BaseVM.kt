package net.wangyl.base.mvi.orbit

import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import net.wangyl.base.extension.getK
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

//import org.orbitmvi.orbit.Container
//import org.orbitmvi.orbit.ContainerHost
//import org.orbitmvi.orbit.syntax.simple.intent
//import org.orbitmvi.orbit.syntax.simple.postSideEffect
//import org.orbitmvi.orbit.viewmodel.container

open class BaseVM<Data : Parcelable>(savedStateHandle: SavedStateHandle) : ViewModel(),
    ContainerHost<BaseState<Data>, Event> {
//open class BaseVM<Data : Parcelable>(savedStateHandle: SavedStateHandle) : ViewModel() {
    //    BaseEvent<PostData>

    val TAG = javaClass.simpleName

    //    private val repository: Repository? = getK()
    val pageInfo = PageInfo()

    var exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("orbit caught the exception ", throwable)
    }
    open var onInit: (ViewModel.() -> Unit)? = null
    private val _container: Container<BaseState<Data>, Event> = container<BaseState<Data>, Event>(
        initialState = BaseState(),
        savedStateHandle = savedStateHandle,
        settings = Container.Settings(exceptionHandler = exceptionHandler)
    ) {
        Timber.d("container oninit = $this")
        onInit?.invoke(this)
    }

    init {
        Timber.d("init BaseVM _container = $_container")
    }

    override val container: Container<BaseState<Data>, Event> = _container

    //增加封装过的参数？确定加载第几页数据，是否要加载下一页数据
//    private fun loadList() = intent {
//        //这里的参数使用封装过的类？
//        val status = loader(0, "")
//
//        reduce {
//            when(status) {
//                //需要在原有数据上添加
//                is Status.Success -> {
//                    //如果为第一次加载，则直接返回
////                    if ()
//                    //如果长度为0，则返回已加载完成
//                    //如果不为0，则添加到原数据后面
//                    state.values.addAll(status.data)
//                    state.copy(values = state.values)
//                }
//                //如果加载失败,返回错误信息
//                is Status.Failure -> state.copy(error = status.exception)
//            }
////            state.copy(values = when(datas) {
////
////                //
////                is Status.Failure -> emptyList()
////            })
//        }
//    }

    //    fun onItemClicked(post: Data) = {
//
//    }
    fun onItemClicked(post: Data) = intent {
        postSideEffect(DetailEvent(post))
    }
}