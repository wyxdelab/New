package com.example.news.component.product

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.util.ResourceUtil.getColorAttributes
import com.example.superui.dropdown.DropMenuItem

/**
 * 下拉菜单列表适配器
 */
class DropDownListMenuAdapter
/**
 * 构造方法
 *
 * @param layoutResId
 */
    () : BaseQuickAdapter<DropMenuItem, QuickViewHolder>() {
    private var selectIndex = 0
    private var textColor = 0
    private var colorPrimary = 0

    fun setSelect(data: Int) {
        if (selectIndex != data) {
            notifyItemChanged(selectIndex)
            selectIndex = data
            notifyItemChanged(selectIndex)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        //这个属性在superui
        colorPrimary = getColorAttributes(
            recyclerView.context,
            com.google.android.material.R.attr.colorPrimary
        )
        textColor = getColorAttributes(
            recyclerView.context,
            com.google.android.material.R.attr.colorOnSurface
        )
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_dropdown, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, data: DropMenuItem?) {
        data?.let {
            if (selectIndex == holder.layoutPosition) {
                holder.setTextColor(R.id.title, colorPrimary)
            } else {
                holder.setTextColor(R.id.title, textColor)
            }
            holder.setText(R.id.title, data.title)
        }
    }
}