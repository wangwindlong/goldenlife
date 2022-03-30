//package net.wangyl.base.interf
//
//import androidx.lifecycle.Observer
//import net.wangyl.base.data.ApiResponse
//
//
//interface IStateObserver<T> : Observer<ApiResponse<T>> {
//
//    override fun onChanged(response: ApiResponse<T>?) {
//        when (response) {
//            is ApiResponse.StartResponse -> {
//                //onStart()回调后不能直接就调用onFinish()，必须等待请求结束
//                onStart()
//                return
//            }
//            is ApiResponse.ApiSuccess -> {
//                if (response.data == null || (response.data is List<*> && response.data.isEmpty())) {
//                    onEmpty()
//                } else onSuccess(response.data)
//            }
//            is ApiResponse.ApiError -> onFailure(response.exception)
//            else -> {}
//        }
//        onFinish()
//    }
//
//    /**
//     * 请求开始
//     */
//    fun onStart()
//
//    /**
//     * 请求成功，且 data 不为 null
//     */
//    fun onSuccess(data: T)
//
//    /**
//     * 请求成功，但 data 为 null 或者 data 是集合类型但为空
//     */
//    fun onEmpty()
//
//    /**
//     * 请求失败
//     */
//    fun onFailure(e: Exception)
//
//    /**
//     * 请求结束
//     */
//    fun onFinish()
//}