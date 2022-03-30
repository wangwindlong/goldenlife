package net.wangyl.base

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import net.wangyl.base.interf.StateHost

/**
 * activity 和 fragment 实现的方法，以供ILifeDelegate 生命周期管理
 */
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
    fun baseDelegate(): ILifeDelegate? = null
    fun uiState(): StateHost? = null
//    fun initTitle(): CharSequence? = ""

}