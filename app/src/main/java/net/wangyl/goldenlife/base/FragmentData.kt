package net.wangyl.goldenlife.base

import android.content.Intent
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FragmentData(val name: String, val title: CharSequence, val extra: Intent? = null) :
    Parcelable
