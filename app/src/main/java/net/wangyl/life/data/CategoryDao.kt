package net.wangyl.life.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import net.wangyl.life.data.entity.CategoryEntity

@Dao
abstract class CategoryDao : EntityDao<CategoryEntity>() {
    @Transaction
    @Query(ENTRY_QUERY_CATEGORIES)
    abstract fun entriesCategory(): Flow<List<CategoryEntity>>


    @Query("DELETE FROM categories WHERE id = :id")
    abstract suspend fun delete(id: Long)

    @Query("DELETE FROM categories")
    abstract suspend fun deleteAll()

    companion object {
        private const val ENTRY_QUERY_CATEGORIES =
            """
            SELECT * FROM categories ORDER BY order_id ASC
        """

        private const val ENTRY_QUERY_ORDER_LAST_WATCHED_FILTER =
            """
            SELECT we.* FROM watched_entries as we
            INNER JOIN shows_fts AS fts ON we.show_id = fts.docid
            WHERE fts.title MATCH :filter
            ORDER BY datetime(last_watched) DESC
        """

        private const val ENTRY_QUERY_ORDER_ALPHA =
            """
            SELECT we.* FROM watched_entries as we
            INNER JOIN shows_fts AS fts ON we.show_id = fts.docid
            ORDER BY title ASC
        """

        private const val ENTRY_QUERY_ORDER_ALPHA_FILTER =
            """
            SELECT we.* FROM watched_entries as we
            INNER JOIN shows_fts AS fts ON we.show_id = fts.docid
            WHERE title MATCH :filter
            ORDER BY title ASC
        """
    }
}
