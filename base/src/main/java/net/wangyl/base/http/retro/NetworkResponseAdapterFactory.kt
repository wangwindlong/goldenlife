package net.wangyl.base.http.retro

import net.wangyl.base.annotation.WrapWith
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.util.checkNotPrimitive
import net.wangyl.base.util.typeEquals
import net.wangyl.base.util.typeToString
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

/**
 * wrapClass 是每个服务端返回的固定格式内容,如果不传则为ApiResponse<Foo>中的Foo类，不进行包裹
 */
class NetworkResponseAdapterFactory(val wrapClass: Class<*>? = null) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<ApiResponse<<Foo>> or Call<ApiResponse<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != ApiResponse::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as ApiResponse<Foo> or ApiResponse<out Foo>" }

        //ApiResponse<Foo> 中的 Foo 类型
        val bodyType = getParameterUpperBound(0, responseType)
//        Timber.d("NetworkResponseAdapterFactory responseType=$responseType wrapedType =$wrapedType")
        //适用于错误时返回数据格式与成功时不一样的情况，需要传两个泛型类型 如 CustomResponse<Data, Err>
//        if (bodyType is ParameterizedType && bodyType.actualTypeArguments.size > 1) {
//            val dataType = getParameterUpperBound(0, bodyType)
//            val errorBodyType = getParameterUpperBound(1, bodyType)
//            val errorBodyConverter = retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)
//            return NetworkResponseAdapter<Any, Any>(dataType, errorBodyConverter)
//        }
        //如果接口有注解WrapData 用这个类名包裹,否则使用注册时传的wrapclass来包裹Foo 如 RSSData<Foo>
        val wrapWith = (annotations.firstOrNull { it is WrapWith } as? WrapWith)?.wrapClass?.java ?: wrapClass
        wrapWith?.let {
            return NetworkResponseAdapter(ParameterizedTypeImpl(null, it, bodyType))
        }
        return NetworkResponseAdapter(bodyType)
    }

}

open class ParameterizedTypeImpl(ownerType: Type?, rawType: Type, vararg typeArguments: Type) :
    ParameterizedType {
    private val ownerType: Type?
    private val rawType: Type
    private val typeArguments: Array<Type>
    override fun getActualTypeArguments(): Array<Type> {
        return typeArguments.clone()
    }

    override fun getRawType(): Type {
        return rawType
    }

    override fun getOwnerType(): Type? {
        return ownerType
    }

    override fun equals(other: Any?): Boolean {
        return other is ParameterizedType && typeEquals(this, other as ParameterizedType?)
    }

    override fun hashCode(): Int {
        return (Arrays.hashCode(typeArguments)
                xor rawType.hashCode()
                xor (ownerType?.hashCode() ?: 0))
    }

    override fun toString(): String {
        if (typeArguments.size == 0) return typeToString(rawType)
        val result = StringBuilder(30 * (typeArguments.size + 1))
        result.append(typeToString(rawType))
        result.append("<").append(typeToString(typeArguments[0]))
        for (i in 1 until typeArguments.size) {
            result.append(", ").append(typeToString(typeArguments[i]))
        }
        return result.append(">").toString()
    }

    init {
        // Require an owner type if the raw type needs it.
        require(
            !(rawType is Class<*>
                    && ownerType == null != (rawType.enclosingClass == null))
        )
        for (typeArgument in typeArguments) {
            Objects.requireNonNull(typeArgument, "typeArgument == null")
            checkNotPrimitive(typeArgument)
        }
        this.ownerType = ownerType
        this.rawType = rawType
        this.typeArguments = typeArguments.clone() as Array<Type>
    }
}