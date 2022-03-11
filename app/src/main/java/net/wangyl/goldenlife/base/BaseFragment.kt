package net.wangyl.goldenlife.base

import android.content.Context
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
    private var isInitData: Boolean =
        false            // flag bit to determine whether the data is initialized
    private var isVisibleToUser: Boolean =
        false       // flags to determine whether fragments are visible
    private var isPrepareView: Boolean =
        false         // flag bit to determine that view has been loaded to avoid null pointer operations


    override fun baseDelegate(): ILifeDelegate? {
        return if (_delegate == null) {
            _delegate = LifeDelegateIml(this)
            _delegate
        } else _delegate
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            lazyInitData()
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
//        savedInstanceState?.apply {
//            toolBar = this.getInt("toolbar_state", toolBar)
//        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lazyInitData() // Load lazily
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createView(inflater, container)
    }

    /**
     * 子类复写创建view
     */
    open fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        isPrepareView = true // At this point the view has been loaded and set to true
    }

    override fun onResume() {
        super.onResume()
        isVisibleToUser = true
        lazyInitData() // Load lazily
    }

    override fun onPause() {
        super.onPause()
        isVisibleToUser = false
    }

    private fun lazyInitData() {
        if (!isInitData && isVisibleToUser && isPrepareView) {
            loadData()
            isInitData = true // Has the data flag been loaded and reassigned to true
        } else if (!isInitData && parentFragment == null && isPrepareView) {
            loadData()
            isInitData = true
        }
    }

    /**
     * 延迟加载数据，子类复写
     */
    open fun loadData() {
    }

    /**
     * 初始化view的操作
     */
    open fun initView(v: View?, savedInstanceState: Bundle? = null) {
//        UltimateBarX.statusBar(this)
//            .fitWindow(true)
//            .colorRes(R.color.grey_100)
//            .light(true)
//            .lvlColorRes(R.color.colorPrimaryText_light)
//            .apply()
    }

    open fun getLayoutId(): Int = R.layout.fragment_common_list

    @Subscribe
    open fun receiveMsg(data: MsgEvent<Any>) {
        Timber.d("$TAG received data: ${data.from}， ${data.msg}")
    }

    override fun refresh(isManualRefresh: Boolean) {

    }
}