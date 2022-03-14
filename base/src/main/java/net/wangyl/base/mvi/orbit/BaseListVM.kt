package net.wangyl.base.mvi.orbit

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle

class BaseListVM<DataClass : Parcelable>(handle: SavedStateHandle) : BaseVM<DataClass>(handle) {
//    BaseEvent<PostData>

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


}