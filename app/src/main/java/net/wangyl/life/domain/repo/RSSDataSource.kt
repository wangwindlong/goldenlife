package net.wangyl.life.domain.repo

import net.wangyl.base.extension.getK
import net.wangyl.base.interf.withRetry
import net.wangyl.life.api.ApiService
import net.wangyl.life.api.getOrThrow
import net.wangyl.life.data.entity.CategoryEntity
import net.wangyl.life.model.Feed
import net.wangyl.life.obj.Global

object RSSDataSource {

    /**
     * 获取所有分类接口
     */
    suspend fun loadCategories(): List<CategoryEntity> = withRetry {
        val stringMap: HashMap<String, String> = hashMapOf(
            "op" to "getCategories",
            "enable_nested" to "true",
            "sid" to (Global.sessionId ?: "tktb7mgjavsn1f1topki9tka5j"),
        )
        getK<ApiService>().categories(stringMap).getOrThrow()
    }

    suspend fun headlines() : List<Feed>  = withRetry {
        val stringMap: HashMap<String, String> = hashMapOf(
            "op" to "getHeadlines",
            "sid" to (Global.sessionId ?: "tktb7mgjavsn1f1topki9tka5j"),
            "sid" to "",
            "feed_id" to "-1",
            "show_excerpt" to "true",
            "excerpt_length" to "256",
            "show_content" to "true",
            "include_attachments" to "true",
            "view_mode" to "adaptive",
            "limit" to "15",
            "offset" to "0",
            "skip" to "0",
            "include_nested" to "true",
            "has_sandbox" to "true",
            "order_by" to "default",
        )

        getK<ApiService>().headlines2(stringMap).getOrThrow()
    }
}