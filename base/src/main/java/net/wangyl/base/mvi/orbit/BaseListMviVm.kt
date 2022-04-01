package net.wangyl.base.mvi.orbit

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineExceptionHandler
import net.wangyl.base.data.ApiResponse
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import timber.log.Timber

class BaseListMviVm<DataClass : Parcelable>(
    val loader: suspend () -> ApiResponse<List<DataClass>>,
    handle: SavedStateHandle,
    exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e("orbit caught the exception ", throwable)
    }
) : BaseMviVm<DataClass>(handle) {
//    BaseEvent<PostData>

    //增加封装过的参数？确定加载第几页数据，是否要加载下一页数据
    private fun loadList() = intent {
        //这里的参数使用封装过的类？
        val status = loader()

        reduce {
            when(status) {
                //需要在原有数据上添加
                is ApiResponse.ApiSuccess -> {
                    //如果为第一次加载，则直接返回
//                    if ()
                    //如果长度为0，则返回已加载完成
                    //如果不为0，则添加到原数据后面
                    state.values.addAll(status.data)
                    state.copy(values = state.values, isFrist = false)
                }
                //如果加载失败,返回错误信息
                is ApiResponse.ApiError -> state.copy(error = status.exception)
                else -> state.copy(isFrist = true, error = null)
            }
//            state.copy(values = when(datas) {
//
//                //
//                is Status.Failure -> emptyList()
//            })
        }
    }


}