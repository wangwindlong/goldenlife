package net.wangyl.life.startup


import android.content.Context
import androidx.startup.Initializer
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber
import com.tencent.mmkv.MMKV
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import net.wangyl.base.widget.toolbar.LoadingStateView
import net.wangyl.base.widget.toolbar.factory
import net.wangyl.eventbus_flow.EventBusInitializer
import net.wangyl.life.*
import net.wangyl.life.delegate.LoadingViewDelegate
import net.wangyl.life.obj.FontManager


@Suppress("unused")
class AppModelInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val rootDir = MMKV.initialize(context.applicationContext)
        Timber.d("MMKV initialize rootDir=$rootDir")
        FontManager.init(context.applicationContext)

//        Bridge.initialize(context.applicationContext, object : SavedStateHandler {
//            override fun saveInstanceState(target: Any, state: Bundle) {
//                Icepick.saveInstanceState(target, state)
//            }
//
//            override fun restoreInstanceState(target: Any, state: Bundle?) {
//                Icepick.restoreInstanceState(target, state)
//            }
//        }, object : ViewSavedStateHandler {
//            override fun <T : View?> saveInstanceState(
//                target: T,
//                parentState: Parcelable?
//            ): Parcelable {
//                return Icepick.saveInstanceState(target, parentState)
//            }
//
//            override fun <T : View?> restoreInstanceState(
//                target: T,
//                state: Parcelable?
//            ): Parcelable? {
//                return Icepick.restoreInstanceState(target, state)
//            }
//        })
        RetrofitUrlManager.getInstance().setDebug(BuildConfig.DEBUG)
        RetrofitUrlManager.getInstance().putDomain(Constants.RSS_DOMAIN_NAME, TTS_BASE_URL)
        EventBusInitializer.init(context.myApp)
        AndroidThreeTen.init(context.myApp)
//        LazyThreeTen.init(context)

        LoadingStateView.factory {
            register(LoadingViewDelegate())
        }
    }

    override fun dependencies() = listOf(
        KoinInitializer::class.java,
        TimberInitializer::class.java,
    )
}


