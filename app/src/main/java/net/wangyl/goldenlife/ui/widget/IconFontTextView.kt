package net.wangyl.goldenlife.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import net.wangyl.goldenlife.GoldApp
import net.wangyl.goldenlife.obj.FontManager

class IconFontTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        typeface = FontManager.get()
    }

}