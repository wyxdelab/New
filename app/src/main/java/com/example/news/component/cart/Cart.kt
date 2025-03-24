package com.example.news.component.cart

import com.example.news.component.product.Product
import com.example.news.entity.Common


class Cart : Common() {
    /**
     * 商品
     */
    lateinit var product: Product

    /**
     * 数量
     */
    var count = 1

    /**
     * 添加到购物车时，使用
     */
    lateinit var productId: String

    /**
     * 是否选中
     */
    var select = true

    /**
     * 切换选择
     */
    fun toggleSelect() {
        select = !select
    }
}