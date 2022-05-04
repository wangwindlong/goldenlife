package net.wangyl.life.startup

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.ktx.Firebase
import net.wangyl.base.BuildConfig
import timber.log.Timber

@Suppress("unused")
class TimberInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        //因为已经在base的 ActivityLifeCycler 中注册了DebugTree 所以此处只需要注册crashlytics日志
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber.DebugTree(), CrashlyticsLogger())
//        } else {
//            Timber.plant(CrashlyticsLogger())
//        }
        if (!BuildConfig.DEBUG) Timber.plant(CrashlyticsLogger())

        Timber.tag("Initializer").d("Timber initialized")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}


class CrashlyticsLogger : Timber.Tree() {
    private val crashlytics by lazy { Firebase.crashlytics }

    override fun isLoggable(tag: String?, priority: Int): Boolean = priority >= Log.INFO

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        t ?: return
        crashlytics.run {
            setCustomKeys {
                key(
                    "priority",
                    when (priority) {
                        Log.INFO -> "Log.INFO"
                        Log.WARN -> "Log.WARN"
                        Log.ERROR -> "Log.ERROR"
                        Log.ASSERT -> "Log.ASSERT"
                        else -> "Log.$priority"
                    }
                )
                key("tag", tag?.ifEmpty { "<empty>" } ?: "<null>")
                key("message", message.ifEmpty { "<empty>" })
                key("throwable_message", t.message ?: "<null>")
            }
            log("[$tag]: $message")
            recordException(t)
        }
    }
}