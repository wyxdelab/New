package com.example.news.component.confirmorder

import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

/**
 * 确认订单详情界面ViewModel
 */
class ConfirmOrderViewModel(private val id: String?, private val carts: ArrayList<String>?) :
    BaseViewModel() {
    private var param: OrderRequest = OrderRequest()

    private val _data = MutableSharedFlow<ConfirmOrderResponse>()
    val data: Flow<ConfirmOrderResponse> = _data

    /**
     * 打开支付界面
     */
    private val _toPay = MutableSharedFlow<String>()
    var toPay: MutableSharedFlow<String> = _toPay

    init {
        //设置商品id
        param.productId = id

        //设置购物车id
        param.carts = carts
    }

    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.confirmOrder(param).onSuccess(viewModel) {
                _data.emit(it!!)

                it!!.address?.let {
                    //设置到创建订单参数对象
                    param.addressId = it.id
                }
            }
        }
    }

    fun setAddressId(data: String) {
        param.addressId = data

        //重新调用服务端计算，因为真实项目中有些商品，不同的收货地址，邮费不一样
        loadData()
    }

    fun createOrder() {
        if (StringUtils.isBlank(param.addressId)) {
            _tip.value = R.string.select_address
            return
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.createOrder(param).onSuccess(viewModel) {
                _toPay.emit(it!!.id!!)
            }
        }
    }

}