package net.wangyl.life.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.get
import kotlinx.coroutines.flow.*
import net.wangyl.base.data.onSuccess
import net.wangyl.base.enums.isLoading
import net.wangyl.base.extension.getK
import net.wangyl.base.extension.inject
import net.wangyl.base.interf.*
import net.wangyl.life.api.ApiService
import net.wangyl.life.api.getOrThrow
import net.wangyl.life.compose.state.DefaultState
import net.wangyl.life.compose.state.NewsState
import net.wangyl.life.domain.actor.UpdateCategory
import net.wangyl.life.domain.createInteractor
import net.wangyl.life.domain.observer.ObsereCategories
import net.wangyl.life.model.Article
import net.wangyl.life.obj.Global
import net.wangyl.life.store.CategoryStore
import net.wangyl.life.store.fetch
import timber.log.Timber

class RSSViewModel(val api: ApiService = getK()) : ViewModel(), StateHost<NewsState> {
    override val stateContainer by stateOf(NewsState())
    //初始化数据库数据，从本地数据库取 如果没有则请求
    val updateCategory = createInteractor<Any> {
        CategoryStore.categoryStore.fetch(0)
    }
    val categoryObserver = ObsereCategories() //category数据库查询的数据流

    val categoryFlow = combine(categoryObserver.flow, updateCategory(Unit)) { categories, loading ->
        NewsState(categories = categories, loading = loading.isLoading())
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = dataFlow.value,
    )

    init {
//        stateContainer.launchApi {
//            return@launchApi NewsState(api.categories(hashMapOf(
//                "op" to "getCategories",
//            )).getOrThrow())
//        }
        categoryObserver(Unit)
    }

    fun getCategories() {
        updateCategory(Unit)
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