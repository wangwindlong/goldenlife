package net.wangyl.base.base

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
    var useEventBus : Boolean
    var showAction : Boolean
    fun setupToolbar(toolbar: Toolbar?) = Unit
    fun setData(data: Any?) = Unit
    val baseDelegate: ILifeDelegate?
    val uiState: StateHost?   //使用变量 by代理模式？
//    fun initTitle(): CharSequence? = ""
}

class BaseImpl: IBase {
    override var useEventBus: Boolean = true
    override var showAction: Boolean = true
    override val baseDelegate: ILifeDelegate? by LifeDelegate()
    override val uiState: StateHost? = null
}
