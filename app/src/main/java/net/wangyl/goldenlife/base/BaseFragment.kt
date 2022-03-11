package net.wangyl.goldenlife.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX
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

    override fun initData(savedInstanceState: Bundle?) {
//        savedInstanceState?.apply {
//            toolBar = this.getInt("toolbar_state", toolBar)
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("FragmentLifecycle onCreate before super create ")
        super.onCreate(savedInstanceState)
        Timber.d("FragmentLifecycle onCreate after super create")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container)
    }

    open fun createView(inflater: LayoutInflater, container: ViewGroup?) : View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView(view, savedInstanceState)
    }

    open fun initView(v: View?, savedInstanceState: Bundle? = null) {
//        UltimateBarX.statusBar(this)
//            .fitWindow(true)
//            .colorRes(R.color.grey_100)
//            .light(true)
//            .lvlColorRes(R.color.colorPrimaryText_light)
//            .apply()
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