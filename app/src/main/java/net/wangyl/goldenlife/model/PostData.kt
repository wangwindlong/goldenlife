package net.wangyl.goldenlife.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostData(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String
) : Parcelable