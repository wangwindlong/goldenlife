package net.wangyl.goldenlife.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import net.wangyl.goldenlife.R

@Parcelize
data class PostData(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) : BaseItem {
    override fun getItemType(): Int {
        return R.layout.item_text_view
    }

    override fun getItemId(): Int {
        return id
    }

    override fun getItemContent(): String {
        return body
    }

}