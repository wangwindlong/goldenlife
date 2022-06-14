package net.wangyl.life.store

import com.dropbox.android.external.store4.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import net.wangyl.base.extension.getK
import net.wangyl.life.data.RSSDatabase
import net.wangyl.life.domain.repo.RSSDataSource
import net.wangyl.life.model.Feed
import net.wangyl.life.utils.DaoManager
import timber.log.Timber
import org.threeten.bp.Duration

object CategoryStore {

    val categoryStore by lazy {
        StoreBuilder.from(
            fetcher = Fetcher.of {
                RSSDataSource.loadCategories()
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = {
                    DaoManager.categoryDao.entriesCategory().map { entries ->
                        Timber.d("categoryStore onread entries=$entries")
                        when {
                            entries.isEmpty() -> { //当本地数据库没有数据时请求远端服务器数据
                                RSSDataSource.loadCategories().also {
                                    DaoManager.categoryDao.withTransaction {
                                        getK<RSSDatabase>().categoryDao().insertOrUpdate(it)
                                    }
                                }
                            }
                            // If the request is expired, our data is stale
//                        lastRequestStore.isRequestExpired(Duration.ofHours(6)) -> null
                            // Otherwise, our data is fresh and valid
                            else -> entries
                        }
                    }
                },
                writer = { id, response ->
                    DaoManager.categoryDao.withTransaction {
                        getK<RSSDatabase>().categoryDao().insertOrUpdate(response)
                    }
                },
                delete = DaoManager.categoryDao::delete,
                deleteAll = DaoManager.categoryDao::deleteAll
            )
        ).build()
    }

    val headLinesStore by lazy {
        StoreBuilder.from(
            fetcher = Fetcher.of {
                RSSDataSource.headlines()
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = {
                    DaoManager.feedsDao.entriesFeeds().map { entries ->
                        Timber.d("entriesFeeds onread entries=$entries")
                        when {
                            entries.isEmpty() -> null
                            // If the request is expired, our data is stale
//                            lastRequestStore.isRequestExpired(Duration.ofHours(24)) -> null
                            // Otherwise, our data is fresh and valid
                            else -> entries
                        }
                    }
                },
                writer = { id, response:List<Feed> ->
                    DaoManager.feedsDao.withTransaction {
                        getK<RSSDatabase>().feedsDao().insertOrUpdate(response)
                    }
                },
                delete = DaoManager.feedsDao::delete,
                deleteAll = DaoManager.feedsDao::deleteAll
            )
        ).build()
    }

}

suspend inline fun <Key : Any, Output : Any> Store<Key, Output>.fetch(
    key: Key,
    forceFresh: Boolean = false
): Output = when {
    forceFresh -> fresh(key)
    else -> get(key)
}

fun <T> Flow<StoreResponse<T>>.filterForResult(): Flow<StoreResponse<T>> = filterNot {
    it is StoreResponse.Loading || it is StoreResponse.NoNewData
}
