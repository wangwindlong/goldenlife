package net.wangyl.life.store

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import kotlinx.coroutines.flow.map
import net.wangyl.base.data.onSuccess
import net.wangyl.base.data.successOr
import net.wangyl.base.extension.getK
import net.wangyl.life.api.ApiService
import net.wangyl.life.data.RSSDatabase
import net.wangyl.life.data.entity.CategoryEntity
import java.time.Duration

object CategoryStore {
//    val categoryStore: Store<Any, List<CategoryEntity>> = StoreBuilder.from(
//        fetcher = Fetcher.of {
//            val stringMap: HashMap<String, String> =  hashMapOf(
//                "op" to "getCategories",
//            )
//            getK<ApiService>().categories(stringMap).successOr { emptyList() }
//        },
//        sourceOfTruth = SourceOfTruth.of(
//            reader = {
//                getK<RSSDatabase>().categoryDao().entriesCategory().map { entries ->
//                    when {
//                        // Store only treats null as 'no value', so convert to null
//                        entries.isEmpty() -> null
//                        // If the request is expired, our data is stale
//                        lastRequestStore.isRequestExpired(Duration.ofHours(6)) -> null
//                        // Otherwise, our data is fresh and valid
//                        else -> entries
//                    }
//
//                }
//            },
//            writer = {
//
//            }
//        )
//    ).build()
}