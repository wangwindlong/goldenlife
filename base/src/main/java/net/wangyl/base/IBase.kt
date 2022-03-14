package net.wangyl.base

import android.os.Bundle
import androidx.appcompat.widget.Toolbar

interface IBase {

//    fun initView(
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View?

    //初始化arguments 数据，不加载数据
    fun initData(savedInstanceState: Bundle?) = Unit
    fun useEventBus() : Boolean = true
    fun showAction() : Boolean = true
    fun setupToolbar(toolbar: Toolbar?) = Unit
    fun setData(data: Any?) = Unit
    fun baseDelegate(): ILifeDelegate?
//    fun initTitle(): CharSequence? = ""

}