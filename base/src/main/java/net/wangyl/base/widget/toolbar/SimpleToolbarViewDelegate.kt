package net.wangyl.base.widget.toolbar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import net.wangyl.base.R
import net.wangyl.base.databinding.BaseLayoutToolbarBinding
import net.wangyl.base.draw.TextDrawable
import timber.log.Timber

class SimpleToolbarViewDelegate : ToolbarViewDelegate() {
  private lateinit var binding: BaseLayoutToolbarBinding

  override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
    binding = BaseLayoutToolbarBinding.inflate(inflater, parent, false)
    (parent.context as? AppCompatActivity)?.setSupportActionBar(binding.baseToolbar)
    return binding.root
  }

  override fun onBindView(view: View, config: ToolbarConfig) {
    binding.apply {
      baseToolbar.title = config.title
      baseToolbar.navigationIcon = TextDrawable(view.resources,
        AppCompatResources.getDrawable(view.context, config.navIcon), config.navText)
//      (view.context as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
      baseToolbar.setNavigationOnClickListener {
        config.navClickListener.onClick(it)
      }
    }
  }
}