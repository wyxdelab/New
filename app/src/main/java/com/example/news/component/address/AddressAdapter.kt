package com.example.news.component.address

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R


/**
 * 收货地址适配器
 */
class AddressAdapter (): BaseQuickAdapter<Address, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, data: Address?) {
        data?.let { data ->
            holder.setVisible(R.id.default_address, data.isDefault())
            holder.setText(R.id.contact, data.getContact())
            holder.setText(R.id.area, data.getReceiverArea())
            holder.setText(R.id.detail, data.detail)
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_address, parent)
    }
}