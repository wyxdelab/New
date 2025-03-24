package com.example.news.component.product

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.config.Config
import com.example.news.util.ImageUtil

class ProductViewHolder(view: View, viewModel: ProductViewModel) : RecyclerView.ViewHolder(view) {
    private val iconView: ImageView = view.findViewById(R.id.icon)
    private val titleView: TextView = view.findViewById(R.id.title)
    private val highlightView: TextView = view.findViewById(R.id.highlight)
    private val priceView: TextView = view.findViewById(R.id.price)
    private val buyCountView: TextView = view.findViewById(R.id.buy_count)

    private var data: Product? = null

//    init {
//        view.setOnClickListener {
//            data?.let {
//                viewModel.detail(it)
//            }
//        }
//    }

    /**
     * 显示数据
     */
    fun bind(data: Product) {
        this.data = data

        ImageUtil.show(iconView, data.icons.get(0))

        if (Config.DEBUG) {
            //添加id是方便查看列表是否有重复数据
            titleView.text = java.lang.String.format("%s-%s", data.id, data.title)
        } else {
            titleView.setText(data.title)
        }

        highlightView.setText(data.highlight)

        //价格
        val price: String = itemView.resources.getString(R.string.price, data.priceFloat)
        priceView.text = price

        buyCountView.setText(
            itemView.getResources().getString(R.string.buy_count, data.ordersCount)
        )
    }
}