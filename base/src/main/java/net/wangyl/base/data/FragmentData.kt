package net.wangyl.base.data

import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FragmentData(val name: String, val title: CharSequence? = null, val extra: Intent? = null) :
    Parcelable
