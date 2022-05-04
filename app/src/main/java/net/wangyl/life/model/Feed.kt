package net.wangyl.life.model

import kotlinx.parcelize.Parcelize
import net.wangyl.base.data.BaseModel


@Parcelize
data class Feed(
    var feed_url: String? = null,
    var title: String? = null,
    var id: Int = 0,
    var unread: Int = 0,
    var has_icon: Boolean = false,
    var cat_id: Int = 0,
    var last_updated: Int = 0,
    var order_id: Int = 0,
    var is_cat: Boolean = false,
    var always_display_as_feed: Boolean = false,
    var display_title: String? = null
) : BaseModel {

    override fun getItemId(): String {
        return "$id"
    }

    override fun getItemContent(): String {
        return title ?: feed_url ?: "$id"
    }

    override fun equals(feed: Any?): Boolean {
        if (feed === this) return true
        return if (feed == null || feed !is Feed) false else
            feed.id == id && (title == null || title == feed.title) && is_cat == feed.is_cat
    }

}
