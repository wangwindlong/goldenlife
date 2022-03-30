package net.wangyl.goldenlife.startup

import android.content.Context
import androidx.startup.Initializer
import net.wangyl.goldenlife.BuildConfig
import net.wangyl.goldenlife.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

@Suppress("unused")
class KoinInitializer : Initializer<Koin> {
  override fun create(context: Context): Koin = context
    .startKoinIfNeeded()
    .also { Timber.tag("Initializer").d("Koin initialized") }

  override fun dependencies(): List<Class<out Initializer<*>>> =
    listOf(TimberInitializer::class.java)
}

/**
 * Start koin if global KoinContext is null.
 * @return [Koin] instance.
 */
fun Context.startKoinIfNeeded(): Koin {
  return GlobalContext.getOrNull() ?: startKoin {
    // use AndroidLogger as Koin Logger
    androidLogger(level = if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)

    // use the Android context given there
    androidContext(applicationContext)

    modules(listOf(mainModule))
  }.koin
}
