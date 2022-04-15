package net.wangyl.base.util

import androidx.collection.LruCache
import net.wangyl.base.Configs
import net.wangyl.base.http.BaseRepository
import timber.log.Timber
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@OptIn(ExperimentalTime::class)
class Cache<K : Any, V> private constructor(
  maxSize: Int,
  private val entryLifetime: Duration,
  private val sizeOf: (key: K, value: V?) -> Int = DefaultSizeCalculator,
) {
  private val timeSource = TimeSource.Monotonic

  private val cache = object : LruCache<K, Value<V>>(maxSize) {
    override fun sizeOf(key: K, value: Value<V>) = sizeOf(key, value.value)
  }

  @Synchronized operator fun get(key: K): V? {
    val value = cache[key] ?: return null
    return if (value.expiredTime.hasPassedNow()) {
      remove(key)
      null
    } else {
      value.value
    }
  }

  fun remove(key: K) {
    cache.remove(key)
  }

  operator fun set(key: K, value: V) {
    cache.put(key, Value(value, timeSource.markNow() + entryLifetime))
  }

  private data class Value<V : Any?>(
    val value: V?,
    val expiredTime: TimeMark,
  )

  private object DefaultSizeCalculator : (Any, Any?) -> Int {
    override fun invoke(p1: Any, p2: Any?): Int = if (p2 == null) 0 else 1
  }

  companion object {
    val cache = Cache<RequestCacheKey, Any?>(
      maxSize = Configs.API_MAX,
      entryLifetime = Configs.API_EXPIRE.seconds
    )
  }
}

fun Cache.Companion.buildCache(method: String, queryItems: Map<String, Any?>) : RequestCacheKey {
  return RequestCacheKey(
    method = method,
    queryItems = queryItems.entries
      .mapNotNull { (k, v) ->
        if (v == null) null
        else k to v.toString()
      }
      .toMap()
  )
}

fun Cache.Companion.getCache(method: String, queryItems: Map<String, Any?>): Any? {
  return cache[buildCache(method, queryItems)]
}

fun Cache.Companion.getCache(requestCacheKey: RequestCacheKey): Any? {
  return cache[requestCacheKey]
}

fun Cache.Companion.saveCache(requestCacheKey: RequestCacheKey?, data: Any?) {
  if (requestCacheKey == null) return
  cache[requestCacheKey] = data
}

data class RequestCacheKey(
  val method: String,
  val queryItems: Map<String, String>,
)

sealed class CacheKey<T>
class HasCache<T>(val cacheKey: T): CacheKey<T>()
class NoCache<T>(val cacheKey: T?): CacheKey<T>()

//加载本地缓存的接口返回数据，移到calladapter里？
inline fun<reified T> hasCache(method: String? = null, params: Map<String, Any?> = emptyMap(),
                               useCache: Boolean = true,
                               hasKey: (T) -> Unit) : CacheKey<RequestCacheKey?> {
  var cacheKey: RequestCacheKey? = null
  if (useCache && method != null && params.isNotEmpty()) {
    cacheKey = Cache.buildCache(method, params)
    val data = Cache.getCache(cacheKey)
    Timber.d("cache cacheKey=$cacheKey get data=$data")

    if (data is T) {
      hasKey(data)
      return HasCache(cacheKey)
    }
  }
  return NoCache(cacheKey)
}

inline fun CacheKey<RequestCacheKey?>.noCache(noCache: RequestCacheKey?.() -> Unit) =
  when (this) {
  is HasCache -> Unit
  is NoCache -> noCache(this.cacheKey)
}
