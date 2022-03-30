package net.wangyl.goldenlife.model

import android.os.Parcelable
//import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.data.BaseItem
import net.wangyl.base.data.ErrorMessage
import net.wangyl.base.interf.Converter

//@JsonClass(generateAdapter = true)
@Parcelize
data class RSSData<T : Parcelable>(
    val content: T?,
    val seq: Int,
    val status: String
) : Converter<T>, Parcelable {

    override fun convert(params: Any?): ApiResponse<T> {
        return if (content != null && seq == 0) ApiResponse.ApiSuccess(content, true, status, status)
        else ApiResponse.ApiError(ErrorMessage(errorMsg = "程序出错", error = status))
    }
}

//@JsonClass(generateAdapter = true)
@Parcelize
data class UserSession(
    val api_level: Int,
    val config: Configs,
    val session_id: String
) : Parcelable

//@JsonClass(generateAdapter = true)
@Parcelize
data class Configs(
    val custom_sort_types: List<BaseItem>,
    val daemon_is_running: Boolean,
    val icons_dir: String,
    val icons_url: String,
    val num_feeds: Int
) : Parcelable

