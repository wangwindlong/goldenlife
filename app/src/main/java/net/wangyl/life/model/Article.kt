package net.wangyl.life.model

import kotlinx.parcelize.Parcelize
import net.wangyl.base.data.BaseModel


@Parcelize
open class Article(
    var id: Long = 0,
    var unread: Boolean = false,
    var marked: Boolean = false,
    var published: Boolean = false,
    var score: Int = 0,
    var updated: Int = 0,
    var is_updated: Boolean = false,
    var title: String? = null,
    var link: String? = null,
    var feed_id: Int = 0,
    var tags: List<String>? = null,
    var attachments: List<Attachment>? = null,
    var content: String? = null,
    var excerpt: String? = null,
    var labels: List<List<String>>? = null,
    var feed_title: String? = null,
    var comments_count: Int = 0,
    var comments_link: String? = null,
    var always_display_attachments: Boolean = false,
    var author: String? = null,
    var note: String? = null,
    var selected: Boolean = false,
    var flavor_image: String? = null,
    var flavor_stream: String? = null,
    var flavor_kind: Int = 0
) : BaseModel {

    override fun getItemId(): String {
        return "$id"
    }

    override fun getItemContent(): String {
        return content ?: title ?: link ?: "$id"
    }
}

@Parcelize
data class Attachment(
    var id: Int = 0,
    var content_url: String? = null,
    var content_type: String? = null,
    var title: String? = null,
    var duration: String? = null,
    var post_id: Int = 0
) : BaseModel {


}