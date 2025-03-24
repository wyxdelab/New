package com.example.news.util

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object SuperRecyclerViewUtil {
    /**
     * 初始化垂直方向LinearLayoutManager RecyclerView
     *
     * @param view
     * @param isAddDivider
     */
    fun initVerticalLinearRecyclerView(view: RecyclerView, isAddDivider: Boolean = false) {
        //尺寸固定
        view.setHasFixedSize(true)

        //布局管理器
        val layoutManager = LinearLayoutManager(view.context)
        view.layoutManager = layoutManager
        if (isAddDivider) {
            //分割线
            val decoration = DividerItemDecoration(view.context, RecyclerView.VERTICAL)
            view.addItemDecoration(decoration)
        }
    }
}