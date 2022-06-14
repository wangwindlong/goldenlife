package net.wangyl.life.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import net.wangyl.life.data.entity.LastRequest
import net.wangyl.life.model.Article

@Dao
abstract class ArticleDao: EntityDao<Article>() {

    @Transaction
    @Query(ENTRY_QUERY_FEEDS)
    abstract fun entriesFeeds(): Flow<List<Article>>

    @Query("DELETE FROM articles WHERE id = :id")
    abstract suspend fun delete(id: Long)

    @Query("DELETE FROM articles")
    abstract suspend fun deleteAll()


    companion object {
        private const val ENTRY_QUERY_FEEDS =
            """
            SELECT * FROM articles ORDER BY updated DESC
        """
    }
}