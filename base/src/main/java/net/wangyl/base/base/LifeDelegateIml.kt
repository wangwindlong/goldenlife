package net.wangyl.base.base

import android.content.Context
import android.os.Bundle
import android.view.View
import net.wangyl.base.manager.EventBusManager
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 生命周期的默认代理实现类，添加eventbus之类
 */
class LifeDelegateIml(private var iBase: IBase?) : ILifeDelegate {
    override fun onAttach(context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (iBase?.useEventBus == true) {
            EventBusManager.get().register(iBase)
        }
    }

    override fun onCreateView(view: View?, savedInstanceState: Bundle?) {
    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onDestroyView() {
    }

    override fun onDestroy() {
        if (iBase?.useEventBus == true) {
            EventBusManager.get().unregister(iBase)
        }
        iBase = null
    }

    override fun onDetach() {
    }

}

class LifeDelegate : ReadOnlyProperty<IBase, ILifeDelegate> {
    private var _delegate: ILifeDelegate? = null

    override fun getValue(thisRef: IBase, property: KProperty<*>): ILifeDelegate {
        return _delegate ?: LifeDelegateIml(thisRef)
    }
}

