package net.wangyl.base.adapter

import android.util.SparseIntArray
import androidx.annotation.LayoutRes
import net.wangyl.base.data.BaseItem
import net.wangyl.base.mvi.orbit.defaultItem

class MultiType<T>(private var layouts: SparseIntArray = SparseIntArray()) {

    private fun registerItemType(type: Int, @LayoutRes layoutResId: Int) {
        this.layouts.put(type, layoutResId)
    }

    fun getItemType(data: T, position: Int): Int {
        return if (data is BaseItem) data.getItemType() else defaultItem
    }

    open fun addItemType(type: Int, @LayoutRes layoutResId: Int): MultiType<T> {
        registerItemType(type, layoutResId)
        return this
    }

}