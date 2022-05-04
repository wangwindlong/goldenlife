package net.wangyl.life.model

import androidx.annotation.DrawableRes
import net.wangyl.life.R

/**
 * {"seq":0,"status":0,"content":{"session_id":"pcm5jk1b9013fclf2h2o0ab3ej","config":
 * {"icons_dir":"feed-icons","icons_url":"feed-icons","daemon_is_running":false,"custom_sort_types":[],"num_feeds":6},"api_level":18}}
 */
data class Sessions(
    val content: Content,
    val seq: Int,
    val status: Int
)

data class Content(
    val api_level: Int,
    val config: Config,
    val session_id: String
)

data class Config(
    val custom_sort_types: List<Any>,
    val daemon_is_running: Boolean,
    val icons_dir: String,
    val icons_url: String,
    val num_feeds: Int
)

class User(
    val id: String,
    val name: String,
    @DrawableRes val avatar: Int
) {
    companion object {
        val Me: User = User("rengwuxian", "扔物线-朱凯", R.drawable.ic_chat_filled)
    }
}