package net.wangyl.life.data.entity

import androidx.room.*
import net.wangyl.base.data.IdEntity
import net.wangyl.life.model.Article
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId


@Entity(
    tableName = "articles",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["tmdb_id"])
    ]
)
data class FeedsEntity(
    @PrimaryKey(autoGenerate = true) override val localid: Long,
) : Article(), IdEntity {
    @Ignore
    constructor() : this(0)

//    @delegate:Ignore
//    val genres by lazy(LazyThreadSafetyMode.NONE) {
//        _genres?.split(",")?.mapNotNull { Genre.fromTraktValue(it.trim()) } ?: emptyList()
//    }

    companion object {
        val EMPTY_SHOW = FeedsEntity()
    }
}