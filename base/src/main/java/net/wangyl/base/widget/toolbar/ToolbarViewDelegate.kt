@file:Suppress("unused")
package net.wangyl.base.widget.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import net.wangyl.base.R
import timber.log.Timber

typealias ToolbarFactory = () -> ToolbarViewDelegate

var toolbarFactory: ToolbarFactory = { SimpleToolbarViewDelegate() }

fun LoadingState.setToolbar(title: CharSequence? = null, navBtnType: NavBtnType = NavBtnType.ICON, block: (ToolbarConfig.() -> Unit)? = null) =
  setHeaders(ToolbarViewDelegate(title, navBtnType, block))

fun LoadingState.updateToolbar(block: ToolbarConfig.() -> Unit) =
  updateView<ToolbarViewDelegate.ViewHolder>(ViewType.TITLE) { update(block) }

fun ToolbarViewDelegate(title: CharSequence? = null, navBtnType: NavBtnType = NavBtnType.ICON, block: (ToolbarConfig.() -> Unit)? = null) =
  toolbarFactory().apply { config = ToolbarConfig(title, navBtnType).apply { block?.invoke(this) } }

abstract class ToolbarViewDelegate : LoadingStateView.ViewDelegate<ToolbarViewDelegate.ViewHolder>(
  ViewType.TITLE) {
  internal lateinit var config: ToolbarConfig

  override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
    ViewHolder(onCreateView(inflater, parent).apply {
      onBindView(this, config) }
    )

  abstract fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View

  abstract fun onBindView(view: View, config: ToolbarConfig)

  inner class ViewHolder(view: View) : LoadingStateView.ViewHolder(view) {
    fun update(block: ToolbarConfig.() -> Unit) = onBindView(rootView, config.apply(block))
  }
}
