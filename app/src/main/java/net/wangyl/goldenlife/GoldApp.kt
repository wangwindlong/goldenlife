package net.wangyl.goldenlife

import android.app.Application
import android.content.Context
import net.wangyl.base.data.Status
import net.wangyl.base.extension.getK
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

val BASE_URL_QUALIFIER = named("BASE_URL")
typealias Loader<T> = suspend () -> Status<T>


//fun getApp(): Context {
//    return GoldApplication.sInstance
//}

val Context.myApp: GoldApp
    get() = applicationContext as GoldApp

class GoldApp : Application() {

    companion object {
        lateinit var sInstance: GoldApp
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GoldApp)
            modules(listOf(mainModule))
        }
        sInstance = this
        registerActivityLifecycleCallbacks(getK())
    }
}

//private inline fun <T:Parcelable>Scope.BaseListVM(
//    loader: suspend () -> Status<Any>,
//    savedStateHandle: SavedStateHandle,
//    repository: Repository
//): Any {
//
//}


