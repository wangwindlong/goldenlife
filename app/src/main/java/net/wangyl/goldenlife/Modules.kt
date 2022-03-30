package net.wangyl.goldenlife

import android.app.Application
import androidx.fragment.app.FragmentManager
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import net.wangyl.base.ActivityLifeCycler
import net.wangyl.base.FragmentLifecycle
import net.wangyl.base.extension.getK
import net.wangyl.base.http.GlobalHttpHandler
import net.wangyl.base.http.GlobalHttpHandlerImpl
import net.wangyl.base.http.retro.NetworkResponseAdapterFactory
import net.wangyl.base.http.retro.converter.GsonConverterFactory
import net.wangyl.goldenlife.api.ApiService
import net.wangyl.goldenlife.api.ApiService.Companion.getUserAgent
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.repo.RSSRepository
import net.wangyl.goldenlife.model.RSSData
import net.wangyl.goldenlife.startup.AnalyticsService
import net.wangyl.goldenlife.startup.AnalyticsServiceImpl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import timber.log.Timber
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
//    single { provideMoshi() }
    single { provideGson() }
    single {
        provideOkHttpClient(0, get())
    }
    single(BASE_URL_QUALIFIER) {
        provideOkHttpClient(1, get())
    }
    single {
        provideRetrofit(
            BASE_URL,
            get(),
            client = get()
        )
    }
    single(BASE_URL_QUALIFIER) {
        provideRetrofit(
            BASE_URL, //get(BASE_URL_QUALIFIER),
            get(),
            client = get(BASE_URL_QUALIFIER)
        )
    }
    single { ApiService(retrofit = get()) }
    //此处可以用 bind FragmentLifecycle::class，就不需要自己实现单例模式了
    single<FragmentManager.FragmentLifecycleCallbacks> { FragmentLifecycle.instance }
//    single<ActivityLifeCycler>(named("test")) bind ActivityLifeCycler::class //此时不需要实现单例模式
    single<Application.ActivityLifecycleCallbacks> { ActivityLifeCycler.instance }

//        viewModel { (oncreate : () -> Unit) -> BaseListVM(get(), get(), onCreate = oncreate) }
//        viewModel { (itemName: String) -> DetailViewModel(get(), itemName, get()) }

    //firebase analytics服务
    single<AnalyticsService> { AnalyticsServiceImpl() }
    single<RSSRepository> { RSSRepository() }
    //application eventviewmodel 初始化
//    single { ViewModelStoreOwner { ViewModelStore() } }
//    single { ViewModelProvider(get<ViewModelStoreOwner>(),
//        ViewModelProvider.AndroidViewModelFactory.getInstance(get())
//    ) }
//    single { get<ViewModelProvider>()[EventViewModel::class.java] }
}


private const val LOG_TAG_HTTP_REQUEST = "okhttp_request"
private const val LOG_TAG_HTTP_RESULT = "okhttp_result"
private const val TIME_OUT_LENGTH = 20L
private fun provideOkHttpClient(type: Int = 0, customIntercept: Interceptor): OkHttpClient {
    //RetrofitUrlManager 初始化
    val builder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
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

//private fun provideMoshi(): Moshi {
//    return Moshi
//        .Builder()
////        .add(RSSMoshiFactory2())
//        .add(RSSMoshiFactory())
//        .add(KotlinJsonAdapterFactory())
//        .build().apply {
//
//        }
//}

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

//private fun provideRetrofit(baseUrl: String, moshi: Moshi, client: OkHttpClient): Retrofit {
//    return Retrofit.Builder()
//        .client(client)
//        .addCallAdapterFactory(NetworkResponseAdapterFactory(RSSData::class.java))
//        .addConverterFactory(MoshiConverterFactory.create(moshi))
//        .baseUrl(baseUrl)
////        .validateEagerly(BuildConfig.DEBUG)
//        .build()
//}

private fun provideRetrofit(baseUrl: String, gson: Gson, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl(baseUrl)
//        .validateEagerly(BuildConfig.DEBUG)
        .build()
}