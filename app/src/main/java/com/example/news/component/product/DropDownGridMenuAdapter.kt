package com.example.news.component.product

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.util.ResourceUtil.getColorAttributes

/**
 * 下拉菜单网络适配器
 */
class DropDownGridMenuAdapter
/**
 * 构造方法
 *
 * @param layoutResId
 */
    () : BaseQuickAdapter<String, QuickViewHolder>() {
    /**
     * 对应的位置是否选中
     * false:未选中，默认
     * true：选中
     */
    private lateinit var selectIndexes: BooleanArray
    private var colorPrimary = 0
    private var textColor = 0

    override fun submitList(list: List<String>?) {
        super.submitList(list)
        selectIndexes = BooleanArray(list!!.size)
    }

    /**
     * 清除所有选择
     */
    fun resetSelect() {
        selectIndexes = BooleanArray(items.size)
        notifyDataSetChanged()
    }

    val select: List<String>
        get() {
            val results: MutableList<String> = ArrayList()
            for (i in selectIndexes!!.indices) {
                if (selectIndexes!![i]) {
                    results.add(getItem(i)!!)
                }
            }
            return results
        }

    fun setSelect(data: Int) {
        selectIndexes!![data] = !selectIndexes!![data]
        notifyItemChanged(data)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        colorPrimary = getColorAttributes(context, com.google.android.material.R.attr.colorPrimary)
        textColor = getColorAttributes(context, com.google.android.material.R.attr.colorOnSurface)
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_dropdown, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, data: String?) {
        data?.let {
            if (selectIndexes != null && selectIndexes!![holder.layoutPosition]) {
                holder.setTextColor(R.id.title, colorPrimary)
            } else {
                holder.setTextColor(R.id.title, textColor)
            }
            holder.setText(R.id.title, data)
        }
    }
}