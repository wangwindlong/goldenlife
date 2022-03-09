package net.wangyl.goldenlife.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import net.wangyl.goldenlife.utils.manager.EventBusManager
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber
import java.lang.reflect.Method

/**
 * 生命周期的默认代理实现类，添加eventbus之类
 */
class LifeDelegateIml(private var iBase: IBase?) : ILifeDelegate {

    override fun onAttach(context: Context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (iBase?.useEventBus() == true) {
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
        if (iBase?.useEventBus() == true) {
            EventBusManager.get().unregister(iBase)
        }
        iBase = null
    }

    override fun onDetach() {
    }

}