package net.wangyl.goldenlife.startup


import android.content.Context
import android.os.Bundle
import androidx.startup.Initializer
import timber.log.Timber
import androidx.annotation.AnyThread
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import net.wangyl.base.extension.getK
import java.util.concurrent.CopyOnWriteArrayList


//@Suppress("unused")
//class AppModelInitializer : Initializer<Unit> {
//    override fun create(context: Context) {
//        context
//            .startKoinIfNeeded()
//            .get<AnalyticsService>()
//            .addProvider(FirebaseAnalyticsProvider)
//        Timber.tag("Initializer").d("AppModelInitializer initialized")
//    }
//
//    override fun dependencies() = listOf(
//        KoinInitializer::class.java,
//        TimberInitializer::class.java,
//    )
//}


