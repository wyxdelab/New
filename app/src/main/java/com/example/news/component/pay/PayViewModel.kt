package com.example.news.component.pay

import androidx.lifecycle.viewModelScope
import com.example.news.AppContext
import com.example.news.R
import com.example.news.component.order.Order
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.Constant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class PayViewModel(val style: Int, val id: String) : BaseViewModel() {
    private val _data = MutableSharedFlow<Order>()
    val data: Flow<Order> = _data

    private val _processAlipay = MutableSharedFlow<String>()
    val processAlipay: Flow<String> = _processAlipay

    /**
     * 支付渠道
     */
    private var channel = Constant.ALIPAY

    lateinit var order: Order

    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.orderDetail(id).onSuccess(viewModel) {
                _data.emit(it!!)
                order = it
            }
        }
    }

    fun setChannel(data: Int) {
        this.channel = data
    }

    /**
     * 获取支付参数
     */
//    fun loadPayData() {
//        //创建参数
//        val data = PayRequest()
//
//        //设置支付渠道
//        data.channel = channel
//
//        viewModelScope.launch(coroutineExceptionHandler) {
//            DefaultNetworkRepository.orderPay(id, data).onSuccess(viewModel) {
//                processPay(it!!)
//            }
//        }
//    }

    /**
     * 处理支付
     *
     * @param data
     */
//    private fun processPay(data: PayResponse) {
//        when (data.channel) {
//            Constant.ALIPAY ->                 //支付宝支付
//                processAlipay(data.pay!!)
//
//            Constant.WECHAT ->                 //微信支付
//                processWechat(data.wechatPay!!)
//
//            else -> _tip.value = R.string.error_pay_channel
//        }
//    }
//
//    private fun processAlipay(data: String) {
//        viewModelScope.launch {
//            _processAlipay.emit(data)
//        }
//    }

    /**
     * 处理微信支付
     *
     * @param data
     */
//    private fun processWechat(data: WechatPay) {
        //把服务端返回的参数
        //设置到对应的字段
//        val request = PayReq()
//        request.appId = data.appid
//        request.partnerId = data.partnerId
//        request.prepayId = data.prepayId
//        request.nonceStr = data.nonceStr
//        request.timeStamp = data.timestamp
//        request.packageValue = data.packageVal
//        request.sign = data.sign
//        AppContext.instance.wxapi.sendReq(request)
//    }


}