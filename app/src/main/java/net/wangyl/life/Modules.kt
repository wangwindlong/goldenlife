package net.wangyl.life

import android.content.Context
import android.os.Debug
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.room.Room
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import net.wangyl.base.FragmentLifecycle
import net.wangyl.base.extension.getK
import net.wangyl.base.http.GlobalHttpHandler
import net.wangyl.base.http.GlobalHttpHandlerImpl
import net.wangyl.base.http.retro.ResponseAdapterFactory
import net.wangyl.base.http.retro.converter.GsonConverterFactory
import net.wangyl.base.model.EventViewModel
import net.wangyl.life.api.ApiService
import net.wangyl.life.api.ApiService.Companion.getUserAgent
import net.wangyl.life.api.Repository
import net.wangyl.life.api.repo.RSSRepository
import net.wangyl.life.data.RSSDatabase
import net.wangyl.life.obj.Global
import net.wangyl.life.startup.AnalyticsService
import net.wangyl.life.startup.AnalyticsServiceImpl
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit

val mainModule = module {
//        single { StreamingClient() }
    single { Repository(get()) }
//    single<Application> { GoldApp.sInstance }
    single<GlobalHttpHandler> { GlobalHttpHandlerImpl(get()) }
    single<Interceptor> {
        HttpLoggingInterceptor().setLevel(
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        )
    }

//    factory { BASE_URL }
//    factory(BASE_URL_QUALIFIER) { BASE_URL }
    single { provideMoshi() }
    single { provideGson() }
    single {
        provideOkHttpClient(0, get(), get())
    }
    single(BASE_URL_QUALIFIER) {
        provideOkHttpClient(1, get(), get())
    }
    single {
        provideRetrofit(
            BASE_URL,
            get<Moshi>(),
            client = get()
        )
    }
    single(BASE_URL_QUALIFIER) {
        provideRetrofit(
            BASE_URL, //get(BASE_URL_QUALIFIER),
            get<Moshi>(),
            client = get(BASE_URL_QUALIFIER)
        )
    }
    single {
        val builder = Room.databaseBuilder(GoldApp.sInstance, RSSDatabase::class.java, Global.DB_NAME)
            .fallbackToDestructiveMigration()
        if (Debug.isDebuggerConnected()) {
            builder.allowMainThreadQueries()
        }
        builder.build()
    }
    single { ApiService(retrofit = get()) }
    //此处可以用 bind FragmentLifecycle::class，就不需要自己实现单例模式了
    single<FragmentManager.FragmentLifecycleCallbacks> { FragmentLifecycle.instance }
//    single<ActivityLifeCycler>(named("test")) bind ActivityLifeCycler::class //此时不需要实现单例模式
//    single<Application.ActivityLifecycleCallbacks> { ActivityLifeCycler.instance }

//        viewModel { (oncreate : () -> Unit) -> BaseListVM(get(), get(), onCreate = oncreate) }
//        viewModel { (itemName: String) -> DetailViewModel(get(), itemName, get()) }

    //firebase analytics服务
    single<AnalyticsService> { AnalyticsServiceImpl() }
    single<RSSRepository> { RSSRepository() }
    //application eventviewmodel 初始化
    single { ViewModelStoreOwner { ViewModelStore() } }
    single { ViewModelProvider(get<ViewModelStoreOwner>(),
        ViewModelProvider.AndroidViewModelFactory.getInstance(get())
    ) }
    single { get<ViewModelProvider>()[EventViewModel::class.java] }
}


private const val LOG_TAG_HTTP_REQUEST = "okhttp_request"
private const val LOG_TAG_HTTP_RESULT = "okhttp_result"
private const val TIME_OUT_LENGTH = 20L
private fun provideOkHttpClient(type: Int = 0, customIntercept: Interceptor, context: Context): OkHttpClient {
    //RetrofitUrlManager 初始化
    val cacheSize = 10 * 1024 * 1024L // 10 MB
    val cache = Cache(context.cacheDir, cacheSize)
    val builder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
        .cache(cache)
        .connectTimeout(TIME_OUT_LENGTH, TimeUnit.SECONDS)
        .callTimeout(TIME_OUT_LENGTH, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT_LENGTH, TimeUnit.SECONDS)
        .writeTimeout(TIME_OUT_LENGTH, TimeUnit.SECONDS)
        .addInterceptor(customIntercept)

//            LoggingInterceptor
//                .Builder()
//                .setLevel(if (BuildConfig.DEBUG) Level.HEADERS else Level.NONE)
//                .log(Platform.INFO)
//                .request(LOG_TAG_HTTP_REQUEST)
//                .response(LOG_TAG_HTTP_RESULT)
//                .build()
//            LoggingInterceptor().apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }

//        .addInterceptor(customIntercept)
    println("provideOkHttpClient called type=$type")
    if (type == 0) builder.addInterceptor(RSSInterceptor())
    if (type == 1) builder.addInterceptor(FilterInterceptor())
    return builder.build()
}

class FilterInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val httpBuilder = originalRequest.url.newBuilder()
        httpBuilder.addEncodedQueryParameter(KEY, KEY_MAP)
        val requestBuilder = originalRequest.newBuilder()
            .url(httpBuilder.build())
        return chain.proceed(requestBuilder.build())
    }

}

class RSSInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val userAgent = getUserAgent(getK())
//        Timber.d("intercept userAgent=$userAgent")
        val request = chain.request()
            .newBuilder()
            .removeHeader("User-Agent")
            .addHeader("User-Agent", userAgent)
//            .addHeader("Authorization", basic("admin", "6629782"))
//            .addHeader("Authorization", basic("wangyl", "Yunlong_782"))
            .build()
        return chain.proceed(request)
    }

}

private fun provideMoshi(): Moshi {
    return Moshi
        .Builder()
//        .add(RSSMoshiFactory2())
//        .add(RSSMoshiFactory())
        .add(KotlinJsonAdapterFactory())
        .build().apply {

        }
}

private fun provideGson(): Gson {
    return GsonBuilder()
        .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
        .setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }

            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return f?.getAnnotation(Expose::class.java) != null
            }

        })
//                .excludeFieldsWithoutExposeAnnotation()
//                .serializeNulls()
        .create()
}

private fun provideRetrofit(baseUrl: String, moshi: Moshi, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(ResponseAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
//        .validateEagerly(BuildConfig.DEBUG)
        .build()
}

private fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(ResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(baseUrl)
//        .validateEagerly(BuildConfig.DEBUG)
        .build()
}