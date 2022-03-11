package net.wangyl.goldenlife.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager

interface IBase {

//    fun initView(
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View?

    //初始化arguments 数据，不加载数据
    fun initData(savedInstanceState: Bundle?) = Unit
    fun useEventBus() : Boolean = true
    fun setupToolbar(toolbar: Toolbar?) = Unit
    fun setData(data: Any?) = Unit
    fun baseDelegate(): ILifeDelegate?
//    fun initTitle(): CharSequence? = ""

}