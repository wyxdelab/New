package com.example.news.component.cart

import android.content.Context
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.util.ImageUtil


/**
 * 购物车列表adapter
 */
class CartAdapter : BaseQuickAdapter<Cart, QuickViewHolder>() {
    var isSelectAll = false
        private set

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        // 返回一个 ViewHolder
        return QuickViewHolder(R.layout.item_cart, parent)
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, d: Cart?) {
        val data = d!!

        //选择状态
        holder.setImageResource(
            R.id.select_icon,
            if (data.select) R.drawable.radio_button_checked else R.drawable.radio_button
        )
        ImageUtil.show(holder.getView(R.id.icon), data.product.icons[0])
        holder.setText(R.id.title, data.product.title)

        //价格
        val price = context.resources.getString(R.string.price, data.product.priceFloat)
        holder.setText(R.id.price, price)

        //数量
        holder.setText(R.id.count, data.count.toString())
    }

    /**
     * 切换选中所有
     */
    fun toggleSelectAll() {
        isSelectAll = !isSelectAll
        for (it in items) {
            it.select = isSelectAll
        }
        notifyItemRangeChanged(0, itemCount)
    }

    fun deleteSelect() {
        val iterator = mutableItems.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().select) {
                iterator.remove()
            }
        }
        notifyDataSetChanged()
    }

    fun cancelSelectAll() {
        for (it in items) {
            it.select = false
        }
        notifyItemRangeChanged(0, itemCount)
    }

    private val mutableItems: MutableList<Cart>
        get() {
            return when (items) {
                is ArrayList -> {
                    items as ArrayList
                }

                is MutableList -> {
                    items as MutableList
                }

                else -> {
                    items.toMutableList().apply { items = this }
                }
            }
        }
}