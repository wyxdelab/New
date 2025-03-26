package com.example.news.component.category

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.util.ImageUtil

class CategoryItemAdapter : BaseQuickAdapter<Category, QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, data: Category?) {
        data?.let {
            holder.setText(R.id.title, data.title)

            ImageUtil.show(holder.getView(R.id.icon), data.icon!!)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_category, parent)
    }
}