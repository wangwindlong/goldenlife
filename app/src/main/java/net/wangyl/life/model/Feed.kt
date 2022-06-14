package net.wangyl.life.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import net.wangyl.base.data.BaseModel
import net.wangyl.base.data.IdEntity

@Entity(
    tableName = "feeds",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["feed_url"])
    ]
)
@Parcelize
open class Feed(
    @PrimaryKey(autoGenerate = true) override var localid: Long,
    override var id: Long = 0,
    var feed_url: String = "",
    var title: String? = null,
    var unread: Int = 0,
    var has_icon: Boolean = false,
    var cat_id: Int = 0,
    var last_updated: Int = 0,
    var order_id: Int = 0,
    var is_cat: Boolean = false,
    var always_display_as_feed: Boolean = false,
    var display_title: String? = null
) : BaseModel, IdEntity {

    override fun getItemId(): String {
        return "$id"
    }

    override fun getItemContent(): String {
        return title ?: feed_url ?: "$id"
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        return if (other == null || other !is Feed) false else
            other.id == id && (title == null || title == other.title) && is_cat == other.is_cat
    }

    override fun hashCode(): Int {
        var result = feed_url.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + id.toInt()
        result = 31 * result + unread
        result = 31 * result + has_icon.hashCode()
        result = 31 * result + cat_id
        result = 31 * result + last_updated
        result = 31 * result + order_id
        result = 31 * result + is_cat.hashCode()
        result = 31 * result + always_display_as_feed.hashCode()
        result = 31 * result + (display_title?.hashCode() ?: 0)
        return result
    }

}
