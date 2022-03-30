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

val CAMEL_REGEX = "(?<=[a-zA-Z])[A-Z]".toRegex()

@Suppress("unused")
class AnalyticsInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        context
            .startKoinIfNeeded()
            .get<AnalyticsService>()
            .addProvider(FirebaseAnalyticsProvider)
        Timber.tag("Initializer").d("Analytics initialized")
    }

    override fun dependencies() = listOf(
        KoinInitializer::class.java,
        TimberInitializer::class.java,
    )
}

data class AnalyticsEvent(val name: String, val params: Map<String, Any>? = null, ) {
    companion object
}

interface AnalyticsService {
    @AnyThread
    fun track(event: AnalyticsEvent)

    @AnyThread
    fun addProvider(provider: AnalyticsProvider)

    @AnyThread
    fun removeProvider(provider: AnalyticsProvider)
}

interface AnalyticsProvider {
    fun track(event: String, params: Map<String, Any>?)
}

class AnalyticsServiceImpl : AnalyticsService {
    private val providers = CopyOnWriteArrayList<AnalyticsProvider>()

    override fun addProvider(provider: AnalyticsProvider) {
        providers += provider
    }

    override fun removeProvider(provider: AnalyticsProvider) {
        providers -= provider
    }

    override fun track(event: AnalyticsEvent) {
        providers.forEach { it.track(event.name, event.params) }
    }
}

object FirebaseAnalyticsEventMapper {
    fun nameFor(event: String): String =
        CAMEL_REGEX.replace(event) { "_" + it.value }.lowercase()

    fun paramsFor(params: Map<String, Any>?): Bundle? {
        return bundleOf(
            *(params ?: return null)
                .mapKeys { nameFor(it.key) }
                .toList()
                .toTypedArray()
        )
    }
}

object FirebaseAnalyticsProvider: AnalyticsProvider {
    private val firebaseAnalytics by lazy { FirebaseAnalytics.getInstance(getK()) }

    override fun track(event: String, params: Map<String, Any>?) {
        firebaseAnalytics.logEvent(
            FirebaseAnalyticsEventMapper.nameFor(event),
            FirebaseAnalyticsEventMapper.paramsFor(params)
        )
    }
}

/*
 * AnalyticsEvents
 */
fun AnalyticsEvent.Companion.downloadChapter(
    chapterLink: String,
    chapterName: String,
    comicLink: String,
    comicName: String,
    elapsedInMilliseconds: Double,
    elapsedInString: String,
): AnalyticsEvent = AnalyticsEvent(
    name = "downloadChapter",
    params = mapOf(
        "chapterLink" to chapterLink,
        "chapterName" to chapterName,
        "comicLink" to comicLink,
        "comicName" to comicName,
        "elapsedInMilliseconds" to elapsedInMilliseconds,
        "elapsedInString" to elapsedInString,
    )
)

fun AnalyticsEvent.Companion.readChapter(
    chapterLink: String,
    chapterName: String,
    imagesSize: Int,
): AnalyticsEvent = AnalyticsEvent(
    name = "readChapter",
    params = mapOf(
        "chapterLink" to chapterLink,
        "chapterName" to chapterName,
        "imagesSize" to imagesSize,
    )
)


