package com.example.news.component.order

import com.example.news.R
import com.example.news.component.address.Address
import com.example.news.component.cart.Cart
import com.example.news.entity.Common
import com.example.news.util.Constant


/**
 * 订单模型
 */
class Order : Common() {
    /**
     * 订单状态
     */
    var status = 0

    /**
     * 商品总价
     */
    var totalPrice: Int = 0

    /**
     * 价格
     */
    var price: Int = 0

    /**
     * 订单来源
     * 一般订单来源不会返回给客户端
     * 我们这样返回来只是给大家演示如何显示这些字段而已
     * 也就是在那个平台创建的订单
     */
    var source = 0

    /**
     * 支付来源
     * 因为可能有创建订单是web网站
     * 用户是在手机上支付的
     */
    var origin = 0

    /**
     * 支付渠道
     */
    var channel = 0

    /**
     * 订单号
     */
    var number: String? = null

    /**
     * 订单所关联的商品信息
     */
    var products: List<Cart>? = null

    /**
     * 地址
     */
    var address: Address? = null

    //region 辅助方法
    val priceFloat
        get() = price * 1.0 / 100

    val totalPriceFloat
        get() = totalPrice * 1.0 / 100

    /**
     * 是否已经支付成功
     *
     * @return
     */
    val isPaid: Boolean
        get() = status >= Constant.WAIT_SHIPPED

    /**
     * 是否是待支付
     *
     * @return
     */
    val isWaitPay: Boolean
        get() = status == Constant.WAIT_PAY

    /**
     * 是否待发货
     *
     * @return
     */
    val isShipped: Boolean
        get() {
            return status == Constant.WAIT_SHIPPED
        }

    /**
     * 支付渠道格式化
     *
     * @return
     */
    fun getChannelFormat(): String? {
        return when (channel) {
            Constant.ALIPAY -> "支付宝"
            Constant.WECHAT -> "微信"
            Constant.HUABEI_STAGE -> "花呗分期"
            else -> ""
        }
    }

    /**
     * 支付来源格式化
     *
     * @return
     */
    fun getOriginFormat(): String? {
        return sourceFormat(origin)
    }

    /**
     * 来源格式化
     *
     * @param data
     * @return
     */
    private fun sourceFormat(data: Int): String {
        return when (data) {
            Constant.ANDROID -> "Android"
            Constant.IOS -> "iOS"
            Constant.WEB -> "Web"
            else -> ""
        }
    }

    /**
     * 订单来源格式化
     *
     * @return
     */
    fun getSourceFormat(): String? {
        return sourceFormat(source)
    }

    /**
     * 获取订单状态
     *
     * @return
     */
    fun getStatusFormat(): Int {
        return when (status) {
            Constant.CLOSE -> R.string.order_close
            Constant.WAIT_SHIPPED -> R.string.wait_shipped
            Constant.WAIT_RECEIVED -> R.string.wait_received
            Constant.WAIT_COMMENT -> R.string.wait_comment
            Constant.COMPLETE -> R.string.complete
            else -> R.string.wait_pay
        }
    }

    /**
     * 获取状态颜色
     *
     * @return
     */
    fun getStatusColor(): Int {
        return when (status) {
            Constant.COMPLETE -> R.color.pass
            else -> R.color.black80
        }
    }
    //endregion
}