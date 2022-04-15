package net.wangyl.goldenlife.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
//import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.BaseItem
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.extension.getK
import net.wangyl.base.interf.Converter
import net.wangyl.base.interf.ErrConverter
import okhttp3.ResponseBody
import java.lang.reflect.Type

//@JsonClass(generateAdapter = true)
//@Parcelize
//data class Error(val error: String): Parcelable

const val RSS_SUCCES = "0"

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
            content != null && status == RSS_SUCCES -> ApiResponse.ApiSuccess(content, true, status, status)
            status != RSS_SUCCES -> ApiResponse.CondError(null, false, msg, status)
            else -> ApiResponse.ApiError(ErrorMessage(errorMsg = "程序出错", error = content?.toString() ?: "服务器未知错误"))
        }
    }
}


//@JsonClass(generateAdapter = true)
@Parcelize
data class UserSession(
    val api_level: Int? = 0,
    val config: Configs? = null,
    val session_id: String? = null
) : BaseItem

//@JsonClass(generateAdapter = true)
@Parcelize
data class Configs(
    val custom_sort_types: List<BaseItem>,
    val daemon_is_running: Boolean,
    val icons_dir: String,
    val icons_url: String,
    val num_feeds: Int
) : BaseItem

