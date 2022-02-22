package net.wangyl.goldenlife.mvi

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.parcelize.TypeParceler

@Parcelize
//@TypeParceler<ExternalClass<T>, ExternalClassParceler>()
data class BaseState<T:Parcelable>(
    val values: List<T> = emptyList(),
    val value: T? = null
): Parcelable

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