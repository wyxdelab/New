package com.example.news.component.confirmorder

import com.example.news.entity.Base
import com.example.news.util.Constant


class OrderRequest : Base() {
    /**
     * 商品id
     */
    var productId: String? = null

    /**
     * 从购物车，选中item购买
     */
    var carts: ArrayList<String>? = null

    /**
     * 地址id
     *
     *
     * 用来客户端传递地址id
     */
    var addressId: String? = null

    /**
     * 创建订单的平台
     * 默认值为android
     * 且不能更改
     * 因为Android平台的来说肯定就是Android
     */
    val source: Int = Constant.ANDROID
}