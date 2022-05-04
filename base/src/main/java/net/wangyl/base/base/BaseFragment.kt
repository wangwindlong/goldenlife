package net.wangyl.base.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import icepick.State
import net.wangyl.base.Configs.APP_NAME
import net.wangyl.base.R
import net.wangyl.base.data.FragmentData
import net.wangyl.base.data.MsgEvent
import net.wangyl.base.extension.gone
import net.wangyl.base.extension.visible
import net.wangyl.base.widget.toolbar.LoadingImpl
import net.wangyl.base.widget.toolbar.LoadingState
import net.wangyl.base.widget.toolbar.NavBtnType
import net.wangyl.base.widget.toolbar.setToolbar
import org.greenrobot.eventbus.Subscribe
import timber.log.Timber


open class BaseFragment : Fragment(), IBase by BaseImpl(), LoadingState by LoadingImpl()  {
    val TAG = javaClass.simpleName
    val MENU_ITEM_ITEM1 = 1

    // flag bit to determine whether the data is initialized
    private var isInitData: Boolean = false

    // flags to determine whether fragments are visible
    private var isVisibleToUser: Boolean = false

    // flag bit to determine that view has been loaded to avoid null pointer operations
    private var isPrepareView: Boolean = false
    @State
    @JvmField var mTitle: String? = null

    var mToolbar: Toolbar? = null
        set(value) {
            if (value == field) return
            field = value
            if (showAction && requireActivity() is AppCompatActivity) {
                (requireActivity() as AppCompatActivity).setSupportActionBar(value)
                value?.visible()
            } else {
                value?.gone()
            }
            value?.setNavigationOnClickListener { requireActivity().onBackPressed() }
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
        mTitle = savedInstanceState?.getString(TAG_TITLE)
        Timber.d("initData savedInstanceState=${savedInstanceState?.keySet()} TAG=$TAG")
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
        val bundle = savedInstanceState ?: arguments
        isDecorated = bundle?.getBoolean(TAG_DECORATE) ?: false
        setHasOptionsMenu(true)

        return createView(inflater, container)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        //do something with your id
        return super.onOptionsItemSelected(item)
    }

    /**
     * 子类复写创建view
     */
    open fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.decorateWithLoadingState(isDecorated)
        if (isDecorated)
            setToolbar(mTitle ?: APP_NAME, NavBtnType.ICON_TEXT) {
                navClickListener = View.OnClickListener { requireActivity().onBackPressed() }
            }
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

    override var showAction = false

    open fun getLayoutId(): Int = R.layout.base_fragment_common_list

    @Subscribe
    open fun receiveMsg(data: MsgEvent<Any>) {
        onReceiveMsg(data)
    }

    open fun onReceiveMsg(data: MsgEvent<Any>) {
        Timber.d("$TAG received data: ${data.from}， ${data.msg}")
    }

    override fun refresh(isManualRefresh: Boolean) {

    }
}


inline fun <T : Fragment> fragmentPage(
    frag: Class<T>, title: String? = "",
    initIntent: Intent.() -> Intent
): FragmentData {
    return FragmentData(frag.name, title, Intent().initIntent())
}

inline fun fragmentPages(
    frags: List<Class<*>>, titles: List<String> = emptyList(),
    initIntent: Intent.(Int) -> Intent
): List<FragmentData> {
    val result = arrayListOf<FragmentData>()
    for ((index, frag) in frags.withIndex()) {
        result.add(FragmentData(frag.name, titles[index], Intent().initIntent(index)))
    }
    return result
}

