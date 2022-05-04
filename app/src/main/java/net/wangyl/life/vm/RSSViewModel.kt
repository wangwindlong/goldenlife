package net.wangyl.life.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import net.wangyl.base.data.onSuccess
import net.wangyl.base.extension.getK
import net.wangyl.base.interf.*
import net.wangyl.life.api.ApiService
import net.wangyl.life.compose.state.DefaultState
import net.wangyl.life.model.Article
import net.wangyl.life.obj.Global
import timber.log.Timber

class RSSViewModel(val api: ApiService = getK()) : ViewModel(), StateHost<DefaultState> {
    override val stateContainer by stateOf(DefaultState)

    init {
        stateContainer.launchApi {
            api.categories(hashMapOf(
                "op" to "getCategories",
            )).onSuccess {
                Timber.d("categories result = ${data}")
            }
            DefaultState
        }
    }

    fun headLines(position: Int? = 0, handler: (List<Article>) -> Unit) {
        val map = HashMap<String, String>()
        map["op"] = "getHeadlines"
        map["sid"] = Global.sessionId ?: "tktb7mgjavsn1f1topki9tka5j" //

        map["feed_id"] = "-1" //-1

        map["show_excerpt"] = "true"
        map["excerpt_length"] = "256" //256

        map["show_content"] = "true"
        map["include_attachments"] = "true"
        map["view_mode"] = "adaptive" //adaptive

        map["limit"] = "15" //15

        map["offset"] = 0.toString()
        map["skip"] = "${position ?: 0}" //0

        map["include_nested"] = "true"
        map["has_sandbox"] = "true"
        map["order_by"] = "default" //default

        launch(viewModelScope) {
            val res = api.headlines(map).onSuccess {
                handler(this.data)
            }
        }
    }

}