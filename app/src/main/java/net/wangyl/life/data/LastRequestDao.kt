package net.wangyl.life.data

import androidx.room.*
import net.wangyl.life.data.entity.LastRequest

@Dao
abstract class LastRequestDao: EntityDao<LastRequest>() {
    @Query("SELECT * FROM last_requests WHERE request = :request AND entity_id = :entityId")
    abstract suspend fun lastRequest(request: String, entityId: Long): LastRequest?

    @Query("SELECT COUNT(*) FROM last_requests WHERE request = :request AND entity_id = :entityId")
    abstract suspend fun requestCount(request: String, entityId: Long): Int

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insert(entity: LastRequest): Long
}