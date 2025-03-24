package com.example.news.component.pay

/**
 * 支付参数
 * 网络请求响应
 */
class PayResponse {
    /**
     * 支付渠道
     */
    var channel = 0

    /**
     * 支付宝参数
     */
    var pay: String? = null

    /**
     * 微信支付参数
     */
    var wechatPay: WechatPay? = null

    override fun toString(): String {
        val sb = StringBuilder("PayResponse{")
        sb.append("channel=").append(channel)
        sb.append(", pay='").append(pay).append('\'')
        sb.append(", wechatPay=").append(wechatPay)
        sb.append('}')
        return sb.toString()
    }
}