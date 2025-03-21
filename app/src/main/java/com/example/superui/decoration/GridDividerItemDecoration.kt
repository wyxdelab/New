/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.example.superui.decoration

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.superui.util.DensityUtil


/**
 * 可自定义分割线样式
 *
 * @author XUE
 * @date 2017/9/10 15:24
 */
class GridDividerItemDecoration(context: Context) : ItemDecoration() {
    private val mDivider: Drawable?

    /**
     * 分割线宽度，默认为1dp
     */
    private var mDividerWidth = 0

    /**
     * 画笔
     */
    private var mPaint: Paint? = null

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        mDividerWidth = mDivider?.intrinsicHeight ?: DensityUtil.dip2px(context, 1f).toInt()
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerWidth
     */
    constructor(context: Context, dividerWidth: Int) : this(context) {
        mDividerWidth = dividerWidth
    }

    /**
     * 自定义分割线
     *
     * @param context
     * @param dividerWidth
     * @param dividerColor
     */
    constructor(context: Context, dividerWidth: Int, dividerColor: Int) : this(context) {
        mDividerWidth = dividerWidth
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = dividerColor
        mPaint!!.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        val spanCount = gridLayoutManager!!.spanCount
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildLayoutPosition(child)
            var column = (position + 1) % 3
            column = if (column == 0) spanCount else column
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top =
                child.bottom + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child))
            val bottom = top + mDividerWidth
            val left =
                child.right + params.rightMargin + Math.round(ViewCompat.getTranslationX(child))
            val right = left + mDividerWidth
            if (mPaint != null) {
                mDivider!!.setBounds(child.left, top, right, bottom)
                mDivider.draw(canvas)
            }
            if (mPaint != null) {
                canvas.drawRect(
                    child.left.toFloat(),
                    top.toFloat(),
                    right.toFloat(),
                    bottom.toFloat(),
                    mPaint!!
                )
            }
            if (column < spanCount) {
                if (mPaint != null) {
                    mDivider!!.setBounds(left, child.top, right, bottom)
                    mDivider.draw(canvas)
                }
                if (mPaint != null) {
                    canvas.drawRect(
                        left.toFloat(),
                        child.top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        mPaint!!
                    )
                }
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        val spanCount = gridLayoutManager!!.spanCount
        val position = parent.getChildLayoutPosition(view)
        val adapter = parent.adapter
        if (adapter != null) {
            val itemCount = parent.adapter!!.itemCount
            val isLastRow = isLastRow(parent, position, spanCount, itemCount)
            val eachWidth = (spanCount - 1) * mDividerWidth / spanCount
            val dl = mDividerWidth - eachWidth
            val left = position % spanCount * dl
            val right = eachWidth - left
            if (isLastRow) {
                outRect[left, 0, right] = 0
            } else {
                outRect[left, 0, right] = mDividerWidth
            }
        } else {
            if ((position + 1) % spanCount > 0) {
                outRect[0, 0, mDividerWidth] = mDividerWidth
            } else {
                outRect[0, 0, 0] = mDividerWidth
            }
        }
    }

    private fun isLastRow(
        parent: RecyclerView,
        pos: Int,
        spanCount: Int,
        childCount: Int
    ): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            // childCount = childCount - childCount % spanCount;
            val lines =
                if (childCount % spanCount == 0) childCount / spanCount else childCount / spanCount + 1
            return lines == pos / spanCount + 1
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                .orientation
            // StaggeredGridLayoutManager 且纵向滚动
            return if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                pos >= childCount
            } else {
                // 如果是最后一行，则不需要绘制底部
                (pos + 1) % spanCount == 0
            }
        }
        return false
    }

    companion object {
        private val ATTRS = intArrayOf(
            R.attr.listDivider
        )
    }
}