import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

//package net.wangyl.goldenlife.moshi
//
//import android.os.Parcelable
//import com.squareup.moshi.*
//import net.wangyl.goldenlife.model.RSSData
//import java.io.IOException
//import java.lang.annotation.RetentionPolicy
//import java.lang.reflect.Type
//
//
//@Retention(AnnotationRetention.RUNTIME)
//@JsonQualifier
//annotation class RSSDATA
//
//
//class RSSMoshiFactory2 : JsonAdapter.Factory {
//    override fun create(
//        type: Type,
//        annotations: MutableSet<out Annotation>,
//        moshi: Moshi
//    ): JsonAdapter<*>? {
//        val annotation = Types.nextAnnotations(annotations, RSSDATA::class.java) ?: return null
//        val envelope: Type = Types.newParameterizedTypeWithOwner(
//            RSSJsonAdapter::class.java,
//            RSSData::class.java, type
//        )
//        val delegate: JsonAdapter<RSSData<*>> = moshi.nextAdapter(this, envelope, annotation)
//        return RSSJsonAdapter(delegate)
//    }
//}
//
//class RSSJsonAdapter(private val delegate: JsonAdapter<RSSData<*>>) : JsonAdapter<Any>() {
//
//    @Throws(IOException::class)
//    override fun fromJson(reader: JsonReader): Any? {
//        return delegate.fromJson(reader)?.data
//    }
//
//    @Throws(IOException::class)
//    override fun toJson(writer: JsonWriter, value: Any?) {
//        delegate.toJson(writer, RSSData(value as Parcelable, 0, "0"))
//    }
//
//
//}
//
//class RSSMoshiAdapter {
//    @FromJson
//    fun fromJson(json: RSSData<*>): ApiResponse<*> {
//        return ApiResponse.ApiSuccess(json.data, success = json.success, msg = json.msg)
//    }
//
//    @ToJson
//    fun toJson(@RSSDATA value: ApiResponse<*>): RSSData<*> {
//        throw UnsupportedOperationException()
//    }
//
//}