package com.example.news.component.confirmorder

import com.example.news.component.address.Address
import com.example.news.component.cart.Cart
import com.example.news.entity.Base


/**
 * 确认订单响应模型
 */
class ConfirmOrderResponse : Base() {
    /**
     * 商品总价
     * 像更改了收货地址（可能产生不同的运费），选择优惠券等情况，本地不要计算，一切都要在服务端计算
     */
    var totalPrice: Int = 0

    /**
     * 还需支付价格
     *
     *
     * 减去优惠券+邮费 后的价格
     */
    var price: Int = 0

    /**
     * 用户传递地址id后，返回的地址对象
     */
    var address: Address? = null

    /**
     * 购物车item
     * 如果是在订单详情购买一个商品，那列表只要一个，并且cart没有id
     */
    lateinit var carts: List<Cart>

    val totalPriceFloat
        get() = totalPrice * 1.0 / 100

    val priceFloat
        get() = price * 1.0 / 100
}