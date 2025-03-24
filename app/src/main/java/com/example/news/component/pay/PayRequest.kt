package com.example.news.component.pay

import com.example.news.util.Constant


/**
 * 请求支付信息参数
 */
class PayRequest {
    /**
     * 支付平台
     * 默认值为android
     * 且不能更改
     * 因为Android平台的来说肯定就是Android
     */
    val origin = Constant.ANDROID

    /**
     * 支付渠道
     */
    var channel = 0
}