package net.wangyl.life

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import net.wangyl.base.ActivityLifeCycler
import net.wangyl.base.data.ApiResponse
import org.koin.core.qualifier.named

val BASE_URL_QUALIFIER = named("BASE_URL")
typealias Loader<T> = suspend () -> ApiResponse<T>

val Context.myApp: GoldApp
    get() = applicationContext as GoldApp

val Fragment.myApp: GoldApp
    get() = requireContext().applicationContext as GoldApp

class GoldApp : Application() {

    companion object {
        lateinit var sInstance: GoldApp
    }

    override fun onCreate() {
        super.onCreate()
//        startKoin {
//            androidContext(this@GoldApp)
//            modules(listOf(mainModule))
//        }
        sInstance = this

        registerActivityLifecycleCallbacks(ActivityLifeCycler.instance)
//        registerActivityLifecycleCallbacks(LoadingActivityLifecycle())
    }

}

//private inline fun <T:Parcelable>Scope.BaseListVM(
//    loader: suspend () -> Status<Any>,
//    savedStateHandle: SavedStateHandle,
//    repository: Repository
//): Any {
//
//}

