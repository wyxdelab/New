package com.example.superui.text

import android.text.TextPaint
import android.text.style.ClickableSpan

/**
 * 自定义ClickableSpan
 * 目的是去除下划线
 */
abstract class SuperClickableSpan : ClickableSpan() {
    /**
     * 更新绘制状态
     *
     * @param ds
     */
    override fun updateDrawState(ds: TextPaint) {
        //只设置颜色
        ds.color = ds.linkColor

        //去掉下划线
        ds.isUnderlineText = false
    }
}