package net.wangyl.goldenlife

import android.app.Application
import android.content.Context
import android.os.Bundle
import com.livefront.bridge.Bridge
import com.livefront.bridge.SavedStateHandler
import icepick.Icepick
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import net.wangyl.base.data.ApiResponse
import net.wangyl.base.extension.getK
import net.wangyl.goldenlife.Constants.TEST_DOMAIN_NAME
import org.koin.core.qualifier.named

val BASE_URL_QUALIFIER = named("BASE_URL")
typealias Loader<T> = suspend () -> ApiResponse<T>


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

//        startKoin {
//            androidContext(this@GoldApp)
//            modules(listOf(mainModule))
//        }
        sInstance = this
        registerActivityLifecycleCallbacks(getK())
//        registerActivityLifecycleCallbacks(LoadingActivityLifecycle())
        Bridge.initialize(applicationContext, object : SavedStateHandler {
            override fun saveInstanceState(target: Any, state: Bundle) {
                Icepick.saveInstanceState(target, state)
            }

            override fun restoreInstanceState(target: Any, state: Bundle?) {
                Icepick.restoreInstanceState(target, state)
            }
        })
        RetrofitUrlManager.getInstance().setDebug(BuildConfig.DEBUG)
        RetrofitUrlManager.getInstance().putDomain(TEST_DOMAIN_NAME, TT_BASE_URL)
    }
}

//private inline fun <T:Parcelable>Scope.BaseListVM(
//    loader: suspend () -> Status<Any>,
//    savedStateHandle: SavedStateHandle,
//    repository: Repository
//): Any {
//
//}


