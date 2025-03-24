package com.example.news.component.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter

import androidx.recyclerview.widget.DiffUtil
import com.example.news.R
import com.example.news.util.Constant


/**
 * 商城适配器
 */
class ProductAdapter(val viewModel: ProductViewModel) :
    PagingDataAdapter<Product, ProductViewHolder>(COMPARATOR) {
    var isListStyle = true

    override fun getItemViewType(position: Int): Int {
        return if (isListStyle) Constant.VALUE0 else Constant.VALUE10
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return if (viewType == Constant.VALUE0) {
            return ProductViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false),
                viewModel
            )
        } else {
            ProductViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_grid, parent, false),
                viewModel
            )
        }
    }

    companion object {
        val COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id
        }
    }

}