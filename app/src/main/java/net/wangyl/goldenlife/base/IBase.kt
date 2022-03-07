package net.wangyl.goldenlife.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager

interface IBase {

//    fun initView(
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View?

    fun initData(savedInstanceState: Bundle?) = Unit
    fun useEventBus() : Boolean = true
    fun setData(data: Any?) = Unit
    fun isAdded(): Boolean
    fun getDelegate(fm: FragmentManager): ILifeDelegate?

}