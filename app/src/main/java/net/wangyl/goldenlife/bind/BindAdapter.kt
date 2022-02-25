package net.wangyl.goldenlife.bind

import androidx.databinding.BindingAdapter
import net.wangyl.goldenlife.ui.widget.ProgressImageButton

@BindingAdapter("showProgress")
fun ProgressImageButton.binding_showProgress(boolean: Boolean) {
    showProgress(boolean)
}