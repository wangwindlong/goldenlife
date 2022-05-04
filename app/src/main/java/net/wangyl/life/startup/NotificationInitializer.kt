package net.wangyl.life.startup


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.startup.Initializer
import net.wangyl.life.BuildConfig
import net.wangyl.life.R
import timber.log.Timber

@Suppress("unused")
class NotificationInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        context.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val appName = getString(R.string.app_name)
                val channel = NotificationChannel(
                    BuildConfig.APPLICATION_ID,
                    appName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "$appName 的通知" }
                getSystemService(NotificationManager::class.java)!!.createNotificationChannel(channel)
            }
        }
        Timber.tag("Initializer").d("Notification initialized")
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(TimberInitializer::class.java)
}
