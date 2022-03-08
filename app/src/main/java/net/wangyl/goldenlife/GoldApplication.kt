package net.wangyl.goldenlife

import android.app.Application
import android.content.Context
import net.wangyl.goldenlife.api.Status
import net.wangyl.goldenlife.base.ActivityLifeCycler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

val BASE_URL_QUALIFIER = named("BASE_URL")
typealias Loader<T> = suspend () -> Status<T>


//fun getApp(): Context {
//    return GoldApplication.sInstance
//}

val Context.myApp: GoldApplication
    get() = applicationContext as GoldApplication

class GoldApplication : Application() {

    companion object {
        lateinit var sInstance: GoldApplication
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GoldApplication)
            modules(listOf(mainModule))
        }
        sInstance = this
        registerActivityLifecycleCallbacks(ActivityLifeCycler.instance)
    }
}

//private inline fun <T:Parcelable>Scope.BaseListVM(
//    loader: suspend () -> Status<Any>,
//    savedStateHandle: SavedStateHandle,
//    repository: Repository
//): Any {
//
//}


