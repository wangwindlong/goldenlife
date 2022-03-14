package net.wangyl.goldenlife

import android.app.Application
import androidx.fragment.app.FragmentManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.wangyl.goldenlife.api.ApiService
import net.wangyl.goldenlife.api.Repository
import net.wangyl.base.ActivityLifeCycler
import net.wangyl.base.FragmentLifecycle
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

val mainModule = module {
//        single { StreamingClient() }
    single { Repository(get()) }
//    single<Application> { get() }

    factory(BASE_URL_QUALIFIER) { BASE_URL }
    single { provideMoshi() }
    single(BASE_URL_QUALIFIER) { provideOkHttpClient() }
    single(BASE_URL_QUALIFIER) {
        provideRetrofit(
            baseUrl = get(BASE_URL_QUALIFIER),
            moshi = get(),
            client = get(BASE_URL_QUALIFIER)
        )
    }
    single { ApiService(retrofit = get(BASE_URL_QUALIFIER)) }
    //此处可以用 bind FragmentLifecycle::class，就不需要自己实现单例模式了
    single<FragmentManager.FragmentLifecycleCallbacks> { net.wangyl.base.FragmentLifecycle.instance }
//    single<ActivityLifeCycler>(named("test")) bind ActivityLifeCycler::class //此时不需要实现单例模式
    single<Application.ActivityLifecycleCallbacks> { net.wangyl.base.ActivityLifeCycler.instance }

//        viewModel { (oncreate : () -> Unit) -> BaseListVM(get(), get(), onCreate = oncreate) }
//        viewModel { (itemName: String) -> DetailViewModel(get(), itemName, get()) }
}


private fun provideMoshi(): Moshi {
    return Moshi
        .Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

private fun provideOkHttpClient(type: Int = 0): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }
        )
    println("provideOkHttpClient called type=$type")
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

private fun provideRetrofit(baseUrl: String, moshi: Moshi, client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(baseUrl)
        .build()
}