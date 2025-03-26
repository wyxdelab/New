package com.example.news.component.search

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R

/**
 * 搜索历史适配器
 */
class SearchHistoryAdapter : BaseQuickAdapter<SearchHistory, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, i: Int, data: SearchHistory?) {
        holder.setText(R.id.title, data!!.content)
    }

    override fun onCreateViewHolder(
        context: Context,
        viewGroup: ViewGroup,
        i: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_search_history, viewGroup)
    }
}