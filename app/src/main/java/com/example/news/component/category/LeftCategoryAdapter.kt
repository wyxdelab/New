package com.example.news.component.category

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.util.ResourceUtil

class LeftCategoryAdapter : BaseQuickAdapter<Category, QuickViewHolder>() {
    private var colorSurface: Int=0
    private var colorSurfaceClick: Int=0
    private var selectedIndex = 0


    fun setSelectedIndex(data: Int) {
        this.selectedIndex = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, data: Category?) {
        data?.let {
            holder.setText(R.id.title, data.title)
            if (selectedIndex == holder.layoutPosition) {
                holder.setVisible(R.id.indicator, true)
                holder.setBackgroundColor(R.id.container, colorSurface)
            } else {
                holder.setVisible(R.id.indicator, false)
                holder.setBackgroundColor(R.id.container, colorSurfaceClick)
            }
        }

    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_left_category, parent)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        colorSurface = ResourceUtil.getColorAttributes(
            recyclerView.context,
            com.google.android.material.R.attr.colorSurface
        )
        colorSurfaceClick = ResourceUtil.getColorAttributes(
            recyclerView.context,
            R.attr.colorSurfaceClick
        )
    }
}