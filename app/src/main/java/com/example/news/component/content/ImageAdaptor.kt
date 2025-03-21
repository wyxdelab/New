package com.example.news.component.content

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.util.ImageUtil

/**
 * 图片Adapter
 */
class ImageAdaptor: BaseQuickAdapter<String, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String?) {
        item?.let {
            val iconView = holder.getView<ImageView>(R.id.icon)
            ImageUtil.show(iconView, it)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_image, parent)
    }
}