package net.wangyl.goldenlife.api

import android.content.Context
import android.content.pm.PackageManager
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import net.wangyl.base.annotation.ErrorWith
import net.wangyl.base.annotation.WrapWith
import net.wangyl.base.data.ApiResponse
import net.wangyl.goldenlife.Constants.TEST_DOMAIN_NAME
import net.wangyl.goldenlife.model.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.*
import java.util.*
import kotlin.reflect.KClass


var HEADER_API_VERSION = "Accept: application/vnd.github.v3+json"

interface ApiService {

    @GET("posts/{id}")
    suspend fun post(@Path("id") id: Int): PostData


    /**
     * 在 Url 的尾部加上 [RetrofitUrlManager.IDENTIFICATION_PATH_SIZE] + PathSize, 表示此 Url 使用超级模式
     * 超级模式是什么? 请看 [RetrofitUrlManager] 的类注释
     * [RetrofitUrlManager.IDENTIFICATION_PATH_SIZE] + 2 表示此 Url 中需要被替换的 BaseUrl 为 '域名/api/data', 它的 PathSize 等于 2
     */
    @GET("posts")
    suspend fun posts(): List<PostData>

    //@Query("since")
    //@FieldMap Map<String, String> fields
//    @Headers(HEADER_API_VERSION)
    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + TEST_DOMAIN_NAME)
    @WrapWith(RSSData::class)
    @POST("api/")
    suspend fun login(
        @Body  body: Map<String, String>,
//        @Field("op") op: String, @Field("user") user: String?,
//        @Field("password") password: String?
    ): ApiResponse<UserSession>

    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + TEST_DOMAIN_NAME)
    @POST("api/")
    suspend fun login2(
        @Body  body: Map<String, String>,
//        @Field("op") op: String, @Field("user") user: String?,
//        @Field("password") password: String?
    ): Any

    @Headers(RetrofitUrlManager.DOMAIN_NAME_HEADER + TEST_DOMAIN_NAME)
    @WrapWith(RSSData::class)
    @POST("api/")
    suspend fun headlines(
        @Body  body: Map<String, String>,
//        @Field("op") op: String, @Field("user") user: String?,
//        @Field("password") password: String?
    ): ApiResponse<List<Article>>

    companion object {
        operator fun invoke(retrofit: Retrofit) = retrofit.create<ApiService>()

        @JvmStatic
        fun jsonBody(vararg params: Pair<String, String>) =
            JSONObject(mapOf(*params)).toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        @JvmStatic
        fun jsonBody(params: Map<String, String>) =
            JSONObject(params).toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        @JvmStatic
        fun getUserAgent(context: Context): String {
            return try {
                val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                String.format(
                    Locale.getDefault(),
                    "Tiny Tiny RSS (Android) %1\$s (%2\$d) %3\$s",
                    packageInfo.versionName,
                    packageInfo.versionCode,
                    System.getProperty("http.agent")
                )
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                String.format(
                    Locale.getDefault(),
                    "Tiny Tiny RSS (Android) Unknown %1\$s",
                    System.getProperty("http.agent")
                )
            }
        }
    }
}