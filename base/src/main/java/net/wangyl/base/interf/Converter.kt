package net.wangyl.base.interf

import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.ErrorMessage
import java.lang.reflect.Type

/**
 * 用来获取数据中的返回值,判断请求是否成功，用于从远程服务器获取数据之后的数据结构实现，对该数据的一个抽象
 * 如：{"ret" :1, "msg":"请求失败", "content":{}} 框架只关心 content中的内容，通过data获取
 * issuccess 请求是否成功，根据ret是否等于0来区分
 * msg 为请求返回的额外信息
 * transform: Custom<T> -> ApiResponse<T>
 */
interface Converter<T> {
    fun convert(params: Any? = null, otherType: Type? = null): ApiResponse<T>
//    val data : T?
//    val success : Boolean
//    val msg: String
//    val other: String
}

/**
 * 接口错误信息返回格式转换
 */
interface ErrConverter<E> {
    fun convert(params: Any? = null): E
//    val data : T?
//    val success : Boolean
//    val msg: String
//    val other: String
}