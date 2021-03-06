package net.wangyl.life.model

import kotlinx.parcelize.Parcelize
import net.wangyl.base.data.BaseItem

@Parcelize
data class PostData(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) : BaseItem {

    //    override fun getItemType(): Int {
//        return R.layout.item_text_view
//    }
    override fun getItemId(): String {
        return "$id"
    }

    override fun getItemContent(): String {
        return body
    }

}

