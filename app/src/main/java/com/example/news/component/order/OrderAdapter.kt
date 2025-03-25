package com.example.news.component.order

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.component.product.Product
import com.example.news.databinding.ItemOrderProductSmallBinding
import com.example.news.util.ImageUtil
import com.example.superui.util.SuperDateUtil

/**
 * 订单列表适配器
 */
class OrderAdapter() : BaseQuickAdapter<Order, QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, data: Order?) {
        data?.let {
            holder.setText(R.id.date, SuperDateUtil.yyyyMMddHHmmss(data.createdAt!!))

            //状态
            holder.setText(R.id.status, data.getStatusFormat())

            //状态颜色
            holder.setTextColor(
                R.id.status, context
                    .getColor(data.getStatusColor())
            )

            //总计
            holder.setText(
                R.id.total,
                context.getString(R.string.total_count, 1)
            )

            //还需支付
            holder.setText(R.id.total_price, context.getString(R.string.price, data.priceFloat))

            //待支付时显示取消订单按钮
            holder.setGone(R.id.cancel_order, !data.isWaitPay)

            //待发货时显示修改地址按钮
            holder.setGone(R.id.change_address, !data.isShipped)

            val productContainer: ViewGroup = holder.getView<ViewGroup>(R.id.product_container)
            productContainer.removeAllViews()

            //显示商品
            var product: Product
            var itemBinding: ItemOrderProductSmallBinding
            for (it in data.products!!) {
                itemBinding = ItemOrderProductSmallBinding.inflate(
                    LayoutInflater.from(context),
                    productContainer, false
                )
                product = it.product

                //图标
                ImageUtil.show(itemBinding.icon, product.icons[0])

                //标题
                itemBinding.title.setText(product.title)

                //价格
                val price = context.resources.getString(R.string.price, product.priceFloat)
                itemBinding.price.text = price

                //数量
                itemBinding.count.text =
                    context.resources.getString(R.string.product_count, it.count)
                productContainer.addView(itemBinding.root)
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_order, parent)
    }
}