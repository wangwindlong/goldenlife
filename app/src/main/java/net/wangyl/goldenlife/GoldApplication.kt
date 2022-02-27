package net.wangyl.goldenlife

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.wangyl.goldenlife.api.ApiService
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

val BASE_URL_QUALIFIER = named("BASE_URL")
typealias Loader<T> = suspend () -> Status<T>
class GoldApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GoldApplication)
            modules(listOf(mainModule))
        }
    }

    private val mainModule = module {
//        single { StreamingClient() }
        single { Repository(get()) }

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

//        viewModel { (loader : Loader<Any>) -> BaseListVM(loader, get(), get()) }
//        viewModel { (itemName: String) -> DetailViewModel(get(), itemName, get()) }
    }
}

//private inline fun <T:Parcelable>Scope.BaseListVM(
//    loader: suspend () -> Status<Any>,
//    savedStateHandle: SavedStateHandle,
//    repository: Repository
//): Any {
//
//}

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

