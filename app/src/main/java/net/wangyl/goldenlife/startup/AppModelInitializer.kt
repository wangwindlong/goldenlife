package net.wangyl.goldenlife.startup


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.startup.Initializer
import timber.log.Timber
import com.livefront.bridge.Bridge
import com.livefront.bridge.SavedStateHandler
import com.livefront.bridge.ViewSavedStateHandler
import com.tencent.mmkv.MMKV
import icepick.Icepick
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import net.wangyl.base.widget.toolbar.LoadingStateView
import net.wangyl.base.widget.toolbar.factory
import net.wangyl.eventbus_flow.EventBusInitializer
import net.wangyl.goldenlife.*
import net.wangyl.goldenlife.delegate.LoadingViewDelegate
import net.wangyl.goldenlife.obj.FontManager


@Suppress("unused")
class AppModelInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        val rootDir = MMKV.initialize(context.applicationContext)
        Timber.d("MMKV initialize rootDir=$rootDir")
        FontManager.init(context.applicationContext)

        Bridge.initialize(context.applicationContext, object : SavedStateHandler {
            override fun saveInstanceState(target: Any, state: Bundle) {
                Icepick.saveInstanceState(target, state)
            }

            override fun restoreInstanceState(target: Any, state: Bundle?) {
                Icepick.restoreInstanceState(target, state)
            }
        }, object : ViewSavedStateHandler {
            override fun <T : View?> saveInstanceState(
                target: T,
                parentState: Parcelable?
            ): Parcelable {
                return Icepick.saveInstanceState(target, parentState)
            }

            override fun <T : View?> restoreInstanceState(
                target: T,
                state: Parcelable?
            ): Parcelable? {
                return Icepick.restoreInstanceState(target, state)
            }
        })
        RetrofitUrlManager.getInstance().setDebug(BuildConfig.DEBUG)
        RetrofitUrlManager.getInstance().putDomain(Constants.TEST_DOMAIN_NAME, TTS_BASE_URL)
        EventBusInitializer.init(context.myApp)

        LoadingStateView.factory {
            register(LoadingViewDelegate())
        }
        Timber.tag("Initializer").d("AppModelInitializer initialized")
    }

    override fun dependencies() = listOf(
        KoinInitializer::class.java,
        TimberInitializer::class.java,
    )
}


