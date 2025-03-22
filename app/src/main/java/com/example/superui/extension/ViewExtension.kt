

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout

/**
 * 显示控件
 */
fun View.show(data: Boolean = true) {
    visibility = if (data) View.VISIBLE else View.GONE
}

/**
 * 是否显示了
 */
fun View.isShow(): Boolean {
    return visibility == View.VISIBLE
}

/**
 * 隐藏控件
 */
fun View.hide() {
    visibility = View.GONE
}

/**
 * 隐藏控件
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 显示或隐藏
 */
fun View.toggle() {
    if (isShow()) {
        hide()
    } else {
        show()
    }
}

/**
 * 重新设置宽高
 */
fun View.resize(width: Int, height: Int) {
    if (layoutParams is ViewGroup.LayoutParams) {
        layoutParams.width = width
        layoutParams.height = height
    }
}

/**
 * 重新设置宽
 */
fun View.setViewWidth(data: Int) {
    if (layoutParams is ViewGroup.LayoutParams) {
        layoutParams.width = data
    }
}

/**
 * 重新设置高
 */
fun View.setViewHeight(data: Int) {
    if (layoutParams is ViewGroup.LayoutParams) {
        layoutParams.height = data
    }
}

/**
 * 重新设置宽高，左边和顶部边距
 */
fun View.resize(width: Int, height: Int, leftMargin: Int, topMargin: Int) {
    if (layoutParams is RelativeLayout.LayoutParams) {
        layoutParams.width = width
        layoutParams.height = height
        (layoutParams as RelativeLayout.LayoutParams).leftMargin = leftMargin
        (layoutParams as RelativeLayout.LayoutParams).topMargin = topMargin
    } else if (layoutParams is FrameLayout.LayoutParams) {
        layoutParams.width = width
        layoutParams.height = height
        (layoutParams as FrameLayout.LayoutParams).leftMargin = leftMargin
        (layoutParams as FrameLayout.LayoutParams).topMargin = topMargin
    }
}

/**
 * 从view创建bitmap(截图)
 *
 * @param view
 * @return
 */
fun View.captureBitmap(): Bitmap? {
    //创建一个和view一样大小的bitmap
    val bitmap =
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    //创建一个画板
    //只是这个画板最终画的内容
    //在Bitmap上
    val canvas = Canvas(bitmap)

    //获取view的背景
    val background = background
    if (background != null) {
        //如果有背景

        //就显示绘制背景
        background.draw(canvas)
    } else {
        //没有背景

        //绘制白色
        canvas.drawColor(Color.WHITE)
    }

    //绘制view内容
    draw(canvas)

    //返回bitmap
    return bitmap
}