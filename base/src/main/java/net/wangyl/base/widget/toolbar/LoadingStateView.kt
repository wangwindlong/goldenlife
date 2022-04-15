package net.wangyl.base.widget.toolbar

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import net.wangyl.base.interf.RefreshEvent
import timber.log.Timber
import java.util.*

/**
 * 加载状态代理类  see https://github.com/DylanCaiCoding/LoadingStateView
 */
class LoadingStateView(private val contentView: View) {
    lateinit var decorView: View private set
    private lateinit var contentParent: ViewGroup //存放装饰器布局的布局容器
    private val parent: ViewGroup? //activity或fragment的根布局
    private var currentViewHolder: ViewHolder? = null
    private var onReloadListener: RefreshEvent? = null
    private var viewDelegates: HashMap<Any, ViewDelegate<*>> = HashMap() //根据viewtype获取到对应的代理
    private val viewHolders: HashMap<Any, ViewHolder> = HashMap() //根据viewtype获取到对应的holder

    /**
     * Constructs a LoadingStateView with a activity and a content view delegate
     *
     * @param activity the activity
     */
    constructor(activity: Activity) :
            this(activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0))

    init {
        viewFactory?.invoke(this)
        parent = contentView.parent as ViewGroup?
        Timber.d("LoadingStateView init parent=$parent")
        register(ContentViewDelegate())
        setDecorView(LinearDecorViewDelegate(listOf()))
    }

    /**
     * Sets an view delegate for decorating content view.
     *
     * @param decorViewDelegate the view delegate for decorating content view.
     */
    fun setDecorView(decorViewDelegate: DecorViewDelegate) {
        currentViewHolder = null
        if (parent != null) {
            val index = parent.indexOfChild(contentView)
            if (index >= 0) {
                parent.removeView(contentView) //如果存在于根布局则移除该 activity的内容布局
            } else {
                parent.removeView(decorView) //如果不存在则移除我们添加的装饰布局
                (contentView.parent as ViewGroup).removeView(contentView)
            }
            decorView = decorViewDelegate.createDecorView() //创建包裹布局
            parent.addView(decorView, index) //添加装饰布局到activity根布局下
        } else {
            decorView = decorViewDelegate.createDecorView()
        }
        contentParent = decorViewDelegate.getContentParent(decorView)
        showView(ViewType.CONTENT)
    }

    /**
     * Adds one or more views to decorate content in the header.
     *
     * @param delegates the view delegates of creating view
     */
    fun setDecorHeader(vararg delegates: ViewDelegate<*>) =
        setDecorView(LinearDecorViewDelegate(delegates.map {
            register(it)
            getViewHolder(it.viewType).rootView
        }))

    /**
     * Adds child decorative view between the content and the decorative view.
     *
     * @param decorViewDelegate the view delegate for decorating content view.
     */
    fun addChildDecorView(decorViewDelegate: DecorViewDelegate) {
        contentParent.removeView(currentViewHolder?.rootView)
        currentViewHolder = null
        val childDecorView = decorViewDelegate.createDecorView()
        contentParent.addView(childDecorView)
        contentParent = decorViewDelegate.getContentParent(childDecorView)
        showView(ViewType.CONTENT)
    }

    /**
     * Adds child decorative header between the content and the decorative view.
     *
     * @param delegates the view delegates of creating view
     */
    fun addChildDecorHeader(vararg delegates: ViewDelegate<*>) =
        addChildDecorView(LinearDecorViewDelegate(delegates.map {
            register(it)
            getViewHolder(it.viewType).rootView
        }))

    private fun DecorViewDelegate.createDecorView() =
        onCreateDecorView(LayoutInflater.from(contentView.context)).also { decorView ->
            contentView.layoutParams?.let { decorView.layoutParams = it }
        }

    /**
     * Registers the view delegate of creating view before showing view.
     *
     * @param viewDelegate  the view delegate of creating view
     */
    fun register(vararg delegates: ViewDelegate<*>) { //初始化viewDelegates 总共支持5种布局模式
        delegates.forEach { viewDelegates[it.viewType] = it }
    }

    /**
     * Called if you need to handle reload event, you can get the listener of reloading data from view holder.
     *
     * @param onReloadListener the listener of reloading data
     */
    fun setOnReloadListener(onReloadListener: RefreshEvent) {
        this.onReloadListener = onReloadListener
    }

    @JvmOverloads
    fun showLoadingView(animation: Animation? = null) = showView(ViewType.LOADING, animation)

    @JvmOverloads
    fun showContentView(animation: Animation? = null) = showView(ViewType.CONTENT, animation)

    @JvmOverloads
    fun showErrorView(animation: Animation? = null) = showView(ViewType.ERROR, animation)

    @JvmOverloads
    fun showEmptyView(animation: Animation? = null) = showView(ViewType.EMPTY, animation)

    /**
     * Shows the view by view type
     *
     * @param viewType the view type of view delegate
     */
    @JvmOverloads
    fun showView(viewType: Any, animation: Animation? = null) {
        val currentViewHolder = currentViewHolder
        if (currentViewHolder == null) {
            addView(viewType)
        } else {
            if (viewHolders[viewType] == null) {
                addView(viewType)
            }
            if (viewType != currentViewHolder.viewType) {
                getViewHolder(viewType).rootView.visibility = View.VISIBLE
                if (animation != null) {
                    animation.onStartHideAnimation(
                        currentViewHolder.rootView,
                        currentViewHolder.viewType
                    )
                    animation.onStartShowAnimation(
                        getViewHolder(viewType).rootView,
                        getViewHolder(viewType).viewType
                    )
                } else {
                    currentViewHolder.rootView.visibility = View.GONE
                }
                this.currentViewHolder = getViewHolder(viewType)
            }
        }
    }

    /**
     * Gets the current view type.
     */
    val currentViewType get() = currentViewHolder!!.viewType

    /**
     * Updates the view by view type. It needs to be called after showing view.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : ViewHolder> updateView(viewType: Any, block: Factory<T>) {
        block.apply { invoke((getViewHolder(viewType) as T)) }
    }

    private fun addView(viewType: Any) {
        val viewHolder = getViewHolder(viewType)
        val rootView = viewHolder.rootView
        if (rootView.parent != null) {
            (rootView.parent as ViewGroup).removeView(rootView)
        }
        if (parent is ConstraintLayout && viewType == ViewType.CONTENT) {
            rootView.updateLayoutParams {
                if (rootView.measuredWidth == 0) width = MATCH_PARENT
                if (rootView.measuredHeight == 0) height = MATCH_PARENT
            }
        }
        contentParent.addView(rootView)
        currentViewHolder = viewHolder
    }

    private fun getViewHolder(viewType: Any): ViewHolder {
        if (viewHolders[viewType] == null) {
            addViewHolder(viewType)
        }
        return viewHolders[viewType] as ViewHolder
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewDelegate<*>> getViewDelegate(viewType: Any) = viewDelegates[viewType] as T

    private fun addViewHolder(viewType: Any) {
        val viewDelegate: ViewDelegate<ViewHolder> = getViewDelegate(viewType)
        val viewHolder = viewDelegate.onCreateViewHolder(
            LayoutInflater.from(contentParent.context),
            contentParent
        )
        viewHolder.viewType = viewType
        viewDelegate.onReloadListener = onReloadListener
        viewHolders[viewType] = viewHolder
    }

    abstract class ViewDelegate<VH : ViewHolder>(val viewType: Any) {
        var onReloadListener: RefreshEvent? = null
            internal set

        abstract fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH
    }

    private inner class ContentViewDelegate :
        LoadingStateView.ViewDelegate<ViewHolder>(ViewType.CONTENT) {
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
            ViewHolder(contentView)
    }

    open class ViewHolder(val rootView: View) {
        internal lateinit var viewType: Any
    }

    abstract class DecorViewDelegate {
        //创建包裹 原始布局的布局，然后添加到activity的根容器下
        abstract fun onCreateDecorView(inflater: LayoutInflater): View

        //获取存放 原始布局内容的 容器
        abstract fun getContentParent(decorView: View): ViewGroup
    }

    private class LinearDecorViewDelegate(private val views: List<View>) : DecorViewDelegate() {
        private lateinit var contentParent: FrameLayout

        override fun onCreateDecorView(inflater: LayoutInflater) =
            LinearLayout(inflater.context).apply {
                orientation = LinearLayout.VERTICAL
                contentParent = FrameLayout(context)
                contentParent.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                views.forEach { addView(it) }
                addView(contentParent)
            }

        override fun getContentParent(decorView: View) = contentParent
    }

    interface Animation {
        fun onStartShowAnimation(view: View, viewType: Any)

        fun onStartHideAnimation(view: View, viewType: Any)
    }

    companion object {
        @JvmStatic
        var viewFactory: Factory<LoadingStateView>? = null
    }
}

//factory
typealias Factory<TR> = TR.() -> Unit

fun LoadingStateView.Companion.factory(factory: Factory<LoadingStateView>) {
    this.viewFactory = factory
}

enum class ViewType {
    TITLE, LOADING, CONTENT, ERROR, EMPTY
}