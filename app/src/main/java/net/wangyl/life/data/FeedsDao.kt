package net.wangyl.life.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.wangyl.life.data.entity.LastRequest
import net.wangyl.life.model.Feed

@Dao
abstract class FeedsDao: EntityDao<Feed>() {


    @Transaction
    @Query(ENTRY_QUERY_FEEDS)
    abstract fun entriesFeeds(): Flow<List<Feed>>

    @Query("SELECT * FROM last_requests WHERE request = :request AND entity_id = :entityId")
    abstract suspend fun lastRequest(request: String, entityId: Long): LastRequest?

    @Query("SELECT COUNT(*) FROM last_requests WHERE request = :request AND entity_id = :entityId")
    abstract suspend fun requestCount(request: String, entityId: Long): Int

    @Query("DELETE FROM feeds WHERE id = :id")
    abstract suspend fun delete(id: Long)

    @Query("DELETE FROM feeds")
    abstract suspend fun deleteAll()


    companion object {
        private const val ENTRY_QUERY_FEEDS =
            """
            SELECT * FROM feeds ORDER BY order_id DESC
        """
    }
}