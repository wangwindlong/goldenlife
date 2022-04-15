package net.wangyl.goldenlife.obj

import android.content.Context
import android.graphics.Typeface

object FontManager {
    private lateinit var typeface: Typeface

    fun init(context: Context) {
        typeface = Typeface.createFromAsset(context.assets, "fonts/iconfont.ttf")
    }

    fun get(): Typeface {
        return typeface
    }
}