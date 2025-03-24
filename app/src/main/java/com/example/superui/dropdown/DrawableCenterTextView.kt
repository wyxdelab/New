package com.example.superui.dropdown

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class DrawableCenterTextView : AppCompatTextView {
    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

    override fun onDraw(canvas: Canvas) {
        val drawables = compoundDrawables
        val leftDrawable = drawables[0] //drawableLeft
        val rightDrawable = drawables[2] //drawableRight
        if (leftDrawable != null || rightDrawable != null) {
            //1,获取text的width
            val textWidth = paint.measureText(text, 0, text.length)
            //2,获取Drawable padding
            val drawablePadding = compoundDrawablePadding
            val drawableWidth: Int
            val bodyWidth: Float
            drawableWidth = //3,获取drawable的宽度
                leftDrawable?.intrinsicWidth ?: rightDrawable!!.intrinsicWidth
            //4,获取绘制区域的总宽度
            bodyWidth = textWidth + drawablePadding + drawableWidth
            if (bodyWidth < width) {
                if (leftDrawable != null) setPadding(
                    0,
                    paddingTop,
                    0,
                    paddingBottom
                ) else  //图片居右设置padding
                    setPadding(0, paddingTop, (width - bodyWidth).toInt(), paddingBottom)
                canvas.translate((width - bodyWidth) / 2, 0f)
            }
        }
        super.onDraw(canvas)
    }
}