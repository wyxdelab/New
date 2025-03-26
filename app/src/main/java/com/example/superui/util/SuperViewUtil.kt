package com.example.superui.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View
import android.view.ViewGroup

object SuperViewUtil {
    /**
     * 从父布局移除
     *
     * @param data
     */
    fun removeFromParent(data: View) {
        (data.parent as ViewGroup).removeView(data)
    }

    /**
     * 从view创建bitmap(截图)
     *
     * @param data
     * @return
     */
    fun captureBitmap(data: View): Bitmap {
        //创建一个和view一样大小的bitmap
        val bitmap = Bitmap.createBitmap(data.width, data.height, Bitmap.Config.ARGB_8888)

        //创建一个画板
        //只是这个画板最终画的内容
        //在Bitmap上
        val canvas = Canvas(bitmap)

        //获取view的背景
        if (data.background != null) {
            //如果有背景

            //就显示绘制背景
            data.background.draw(canvas)
        } else {
            //没有背景

            //绘制白色
            canvas.drawColor(Color.WHITE)
        }

        //绘制view内容
        data.draw(canvas)

        //返回bitmap
        return bitmap
    }

    /**
     * 重新设置宽高
     */
    fun resize(view: View, width: Int, height: Int) {
        val layoutParams = view.layoutParams
        layoutParams.width = width
        layoutParams.height = height
    }
}