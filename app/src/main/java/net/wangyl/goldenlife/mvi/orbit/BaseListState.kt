package net.wangyl.goldenlife.mvi.orbit

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
//@TypeParceler<ExternalClass<T>, ExternalClassParceler>()
data class BaseState<T : Parcelable>(
    val values: List<T> = arrayListOf(),
    val value: T? = null,
    val error: Throwable? = null,
    val isEnd: Boolean = false,
    val _count: Long = 0
) : Parcelable

//@Parcelize
////@TypeParceler<ExternalClass, ExternalClassParceler>()
//data class DefaultState(
//    val values: List<String> = emptyList(),
//    val value: T? = null
//) : Parcelable

//class ExternalClass<T>(val value: T)
//
//object ExternalClassParceler : Parceler<ExternalClass> {
//    override fun create(parcel: Parcel) = ExternalClass(parcel.readInt())
//
//    override fun ExternalClass.write(parcel: Parcel, flags: Int) {
//        parcel.writeInt(value)
//    }
//}