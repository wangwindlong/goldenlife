//package net.wangyl.goldenlife.moshi
//
//import android.os.Parcelable
//import com.squareup.moshi.*
//import net.wangyl.base.data.ApiResponse
//import net.wangyl.goldenlife.model.RSSData
//import timber.log.Timber
//import java.lang.Exception
//import java.lang.reflect.ParameterizedType
//import java.lang.reflect.Type
//import  kotlin.annotation.AnnotationTarget.TYPE
//
//@Target(TYPE)
//@Retention(AnnotationRetention.RUNTIME)
//annotation class AlwaysSerializeNulls
//
//class RSSMoshiFactory : JsonAdapter.Factory {
//    override fun create(
//        type: Type,
//        annotations: MutableSet<out Annotation>,
//        moshi: Moshi
//    ): JsonAdapter<*>? {
//        val rawType = type.rawType
//        //序列化空字符串
////        if (!rawType.isAnnotationPresent(AlwaysSerializeNulls::class.java)) {
////            return null
////        }
////        val delegate: JsonAdapter<Any> = moshi.nextAdapter(this, type, annotations)
////        delegate.serializeNulls()
//
//        Timber.d("RSSMoshiFactory create rawType=$rawType")
//        if (rawType !is ParameterizedType) {
//
//        }
//        //判断是否为RSSData<T>
//        if (rawType != RSSData::class.java) return null
//        Timber.d("RSSMoshiFactory create rawType is RSSData type=${type is ParameterizedType}")
//        //获取 ApiResponse 的泛型参数
//        check(type is ParameterizedType) { "返回值ApiResponse必须是ApiResponse<xxx>格式 " }
//        val dataType: Type = type.actualTypeArguments.firstOrNull() ?: return null
//        Timber.d("RSSMoshiFactory create RSSData dataType=$dataType")
//        //获取 ApiResponse<xxx>中xxx 的 JsonAdapter
//        val dataTypeAdapter = moshi.nextAdapter<Any>(this, dataType, annotations)
//        return ApiResultTypeAdapter(rawType, dataTypeAdapter)
//    }
//}
//
//class ApiResultTypeAdapter(
//    private val outerType: Type,
//    private val dataTypeAdapter: JsonAdapter<*>
//) : JsonAdapter<RSSData<*>>() {
//    init {
//        Timber.d("ApiResultTypeAdapter init")
//    }
//    override fun fromJson(reader: JsonReader): RSSData<*>? {
//        //开始读取数据
//        reader.beginObject()
//        var code : Int? = 0
//        var msg: String? = null
//        var data: Any? = null
//        //可空的stringAdapter
//        val nullableStringAdapter: JsonAdapter<String?> = Moshi.Builder().build().adapter(
//            String::class.java,
//            emptySet(), "message"
//        )
//        //同样是分段读取
//        while (reader.hasNext()) {
//            val nextName = reader.nextName()
//            Timber.d("ApiResultTypeAdapter fromJson reader.nextName()=${nextName}")
//            when (nextName) {
//                "seq" -> code = reader.nextString().toIntOrNull()
//                "status" -> msg = nullableStringAdapter.fromJson(reader)
//                "content" -> data = dataTypeAdapter.fromJson(reader)
//                else -> reader.skipValue()
//            }
//        }
//        reader.endObject()
//
//        return if (code != 0) RSSData(null, code ?: 0, msg ?: "")
//        else RSSData(data as Parcelable, 0, msg ?: "")
//    }
//
//    // 不需要序列化的逻辑
//    override fun toJson(writer: JsonWriter, value: RSSData<*>?): Unit {
//
//    }
//}
//
//fun main() {
//    val json = """{
//    "name": "张三",
//    "age": 18,
//    "email": "helloword@163.com"}"""
//
//    // Model类
//    class People(val name: String, val age: String) {
//        val email: String = ""
//    }
//
//    // 反序列化
//    fun parseModel(json: String): People? {
//        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
//        val adapter = moshi.adapter(People::class.java)
//        val people = adapter.fromJson(json)
//        return people
//    }
//
//    parseModel(json)
//}