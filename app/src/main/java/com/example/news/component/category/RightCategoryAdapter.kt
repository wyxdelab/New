package com.example.news.component.category

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.superui.decoration.GridDividerItemDecoration
import com.example.superui.util.DensityUtil

class RightCategoryAdapter : BaseQuickAdapter<Category, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, data: Category?) {
        data?.let {
            holder.setText(R.id.title, data.title)

            val list = holder.getView<RecyclerView>(R.id.list)
            var adapter = list.adapter as? CategoryItemAdapter
            if (adapter == null) {
                list.layoutManager = GridLayoutManager(context, 3)

                //分割线
                val itemDecoration =
                    GridDividerItemDecoration(context, DensityUtil.dip2px(context, 10f).toInt())
                list.addItemDecoration(itemDecoration)

                adapter = CategoryItemAdapter()
                list.adapter = adapter
            }

            adapter.submitList(data.data)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_right_category, parent)
    }
}