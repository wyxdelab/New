package com.example.news.component.pay

import com.google.gson.annotations.SerializedName

/**
 * 微信支付参数（服务端返回）
 *
 *
 * 字段解释：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_1
 */
class WechatPay {
    var appid: String? = null

    @SerializedName("partnerId")
    var partnerId: String? = null

    @SerializedName("prepayId")
    var prepayId: String? = null

    @SerializedName("nonceStr")
    var nonceStr: String? = null

    var timestamp: String? = null

    @SerializedName("packageVal")
    var packageVal: String? = null
    var sign: String? = null
}