package net.wangyl.goldenlife.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.wangyl.goldenlife.R
import net.wangyl.goldenlife.mvi.orbit.RefreshEvent
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber

open class BaseFragment : Fragment(), IBase, RefreshEvent {
    val TAG = javaClass.simpleName
    private var _delegate: ILifeDelegate? = null

    override fun baseDelegate(): ILifeDelegate? {
        return if (_delegate == null) {
            _delegate = LifeDelegateIml(this)
            _delegate
        } else _delegate
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = createView(inflater, container)
        initView(v)
        return v
    }

    open fun createView(inflater: LayoutInflater, container: ViewGroup?) : View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    open fun initView(v: View?) {

    }

    open fun getLayoutId(): Int {
        return R.layout.fragment_common_list
    }

    @Subscribe
    open fun receiveMsg(data: MsgEvent<Any>) {
        Timber.d("$TAG received data: ${data.from}， ${data.msg}")
    }

    override fun refresh(isManualRefresh: Boolean) {

    }
}