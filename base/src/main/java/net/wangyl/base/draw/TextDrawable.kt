package net.wangyl.base.draw

import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import net.wangyl.base.extension.dp2px
import kotlin.math.max

class TextDrawable(res: Resources, icon: Drawable? = null, text: CharSequence? = "") : Drawable() {
    private val DEFAULT_COLOR: Int = Color.BLACK
    private val DEFAULT_TEXTSIZE = 15f
    private val DEFAULT_PADDING = 6.dp2px() //icon距离做边界距离 dp

    var mPaint: Paint
    private var mText: CharSequence? = null
    private var mIcon: Drawable? = null
    private var mIntrinsicWidth = 0
    private var mIntrinsicHeight = 0
    private var mIconWidth = 0
    private var mIconHeight = 0
    private var mTextDistance = 0f

    init {
        mText = text
        mIcon = icon
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color = DEFAULT_COLOR
        mPaint.textAlign = Paint.Align.CENTER
        val textSize: Float = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            DEFAULT_TEXTSIZE, res.displayMetrics
        )
        mPaint.textSize = textSize
        if (mText?.isNotEmpty() == true) {
            mIntrinsicWidth = (mPaint.measureText(mText, 0, mText!!.length) + 5.5).toInt()
        }
        if (mIcon != null) {
            mIconWidth = mIcon!!.intrinsicWidth
            mIconHeight = mIcon!!.intrinsicHeight
            mIntrinsicWidth += mIconWidth
        }
        mIntrinsicWidth += DEFAULT_PADDING
        mIntrinsicHeight = max(mIconHeight, mPaint.getFontMetricsInt(null))
        //计算baseline
        val fontMetrics: Paint.FontMetrics = mPaint.fontMetrics
        mTextDistance = (fontMetrics.bottom + fontMetrics.top) / 2
    }

    override fun draw(canvas: Canvas) {
        val bounds: Rect = bounds
        if (mIcon != null) {
            val top = (bounds.bottom - bounds.top - mIconHeight) / 2
            mIcon!!.setBounds(DEFAULT_PADDING, top, mIconWidth + DEFAULT_PADDING, mIconHeight + top)
            mIcon!!.draw(canvas)
        }
        if (mText != null) canvas.drawText(
            mText!!, 0, mText!!.length,
            bounds.centerX().toFloat() + (mIntrinsicWidth - mIconWidth - DEFAULT_PADDING) / 2 - 3,
            bounds.centerY().toFloat() - mTextDistance, mPaint
        )
    }

    override fun getOpacity(): Int = mPaint.alpha

    override fun getIntrinsicWidth(): Int = mIntrinsicWidth

    override fun getIntrinsicHeight(): Int = mIntrinsicHeight

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(filter: ColorFilter?) {
        mPaint.colorFilter = filter
    }
}