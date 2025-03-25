package com.example.news.util

import android.app.Activity
import com.alipay.sdk.app.PayTask
import com.drake.channel.sendEvent
import com.example.news.component.pay.AlipayStatusChangedEvent
import com.example.news.component.pay.PayResult

/**
 * 支付工具类
 */
object PayUtil {
    private const val TAG = "PayUtil"

    /**
     * 支付宝支付
     * 支付宝官方开发文档：https://docs.open.alipay.com/204/105295/
     *
     * @param activity
     * @param data
     */
//    fun alipay(activity: Activity, data: String) {
//        //创建运行对象
//        val runnable = Runnable { //创建支付宝支付任务
//            val alipay = PayTask(activity)
//
//            //支付结果
//            val result = alipay.payV2(data, true)
//
//            //解析支付结果
//            val resultData = PayResult(result)
//
//            //发布状态
//            sendEvent(
//                AlipayStatusChangedEvent(resultData)
//            )
//        }

//        //创建一个线程
//        val thread = Thread(runnable)
//
//        //启动线程
//        thread.start()
//    }
}