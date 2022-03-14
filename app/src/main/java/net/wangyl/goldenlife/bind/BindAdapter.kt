package net.wangyl.goldenlife.bind

import androidx.databinding.BindingAdapter
import net.wangyl.base.widget.ProgressImageButton

@BindingAdapter("showProgress")
fun ProgressImageButton.binding_showProgress(boolean: Boolean) {
    showProgress(boolean)
}