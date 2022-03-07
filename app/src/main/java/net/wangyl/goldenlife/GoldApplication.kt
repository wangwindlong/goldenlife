package net.wangyl.goldenlife

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import net.wangyl.goldenlife.api.ApiService
import net.wangyl.goldenlife.api.Repository
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.mvi.base.BaseListVM
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

val BASE_URL_QUALIFIER = named("BASE_URL")
typealias Loader<T> = suspend () -> Status<T>


//fun getApp(): Context {
//    return GoldApplication.sInstance
//}

val Context.myApp: GoldApplication
    get() = applicationContext as GoldApplication

class GoldApplication : Application(), Application.ActivityLifecycleCallbacks {
    /**
     * 管理所有存活的 Activity, 容器中的顺序仅仅是 Activity 的创建顺序, 并不能保证和 Activity 任务栈顺序一致
     */
    private val mActivityList: List<Activity> = LinkedList()

    /**
     * 当前在前台的 Activity
     */
    private val mCurrentActivity: Activity? = null

    companion object {
//        lateinit var sInstance: GoldApplication

    }

    init {
//        sInstance = this
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GoldApplication)
            modules(listOf(mainModule))
        }

        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityResumed(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityPaused(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(activity: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(activity: Activity) {
        TODO("Not yet implemented")
    }
}

//private inline fun <T:Parcelable>Scope.BaseListVM(
//    loader: suspend () -> Status<Any>,
//    savedStateHandle: SavedStateHandle,
//    repository: Repository
//): Any {
//
//}


