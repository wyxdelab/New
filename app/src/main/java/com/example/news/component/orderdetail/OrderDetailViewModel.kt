package com.example.news.component.orderdetail

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.news.component.order.Order
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.Constant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class OrderDetailViewModel(val id: String, private val style: Int?) : BaseViewModel(), DefaultLifecycleObserver {
    private val _data = MutableSharedFlow<Order>()
    val data: Flow<Order> = _data


    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.orderDetail(id).onSuccess(viewModel) {
                if (style != null) {
                    it!!.status = Constant.WAIT_SHIPPED
                }
                _data.emit(it!!)
            }
        }
    }

}