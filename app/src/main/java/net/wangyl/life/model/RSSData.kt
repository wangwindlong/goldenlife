package net.wangyl.life.model

//import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.BaseItem
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.interf.Converter
import java.lang.reflect.Type

//@JsonClass(generateAdapter = true)
//@Parcelize
//data class Error(val error: String): Parcelable

const val RSS_SUCCES = "0"
const val SERVER_ERROR = "未知错误"
const val NETWORK_ERROR = "网络异常"

data class RSSError(val error: String) {
}

data class RSSData<T>(
    val content: T?,
    val seq: Int,
    val status: String,
    val msg: String?,
) : Converter<T> {

    override fun convert(params: Any?, otherType: Type?): ApiResponse<T> {
        return when {
            content != null && status == RSS_SUCCES -> ApiResponse.ApiSuccess(
                content,
                true,
                status,
                status
            )
            status != RSS_SUCCES -> ApiResponse.CondError(null, false, msg, status)
            else -> ApiResponse.ApiError(
                ErrorMessage(
                    errorMsg = SERVER_ERROR,
                    error = content?.toString() ?: SERVER_ERROR
                )
            )
        }
    }
}


//@JsonClass(generateAdapter = true)
@Parcelize
data class UserSession(
    var api_level: Int? = 0,
    var config: Configs? = null,
    var session_id: String? = null
) : BaseItem {
    fun clear() {
        api_level = 0
        config = null
        session_id = null
    }

}

//@JsonClass(generateAdapter = true)
@Parcelize
data class Configs(
//    val custom_sort_types: List<BaseItem>,
    val daemon_is_running: Boolean,
    val icons_dir: String,
    val icons_url: String,
    val num_feeds: Int
) : BaseItem

