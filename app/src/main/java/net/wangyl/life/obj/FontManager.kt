package net.wangyl.life.obj

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import net.wangyl.life.R

object FontManager {
    private var typeface: Typeface? = null

    fun init(context: Context) {
//        typeface = Typeface.createFromAsset(context.assets, "font/iconfont.ttf")
        typeface = ResourcesCompat.getFont(context, R.font.iconfont)

    }

    fun get(): Typeface? {
        return typeface
    }
}