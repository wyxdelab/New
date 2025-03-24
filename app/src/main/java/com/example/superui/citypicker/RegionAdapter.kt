package com.example.superui.citypicker

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R

class RegionAdapter() : BaseQuickAdapter<Region, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, i: Int, region: Region?) {
        holder.setText(R.id.name, region!!.name)
    }

    override fun onCreateViewHolder(
        context: Context,
        viewGroup: ViewGroup,
        i: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.superui_item_city, viewGroup)
    }
}