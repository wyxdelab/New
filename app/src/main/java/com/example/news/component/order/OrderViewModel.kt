package com.example.news.component.order

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class OrderViewModel(val status: Int) : BaseViewModel(), DefaultLifecycleObserver {
    private val _data = MutableSharedFlow<List<Order>>()
    val data: Flow<List<Order>> = _data

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.orders(status).onSuccess(viewModel) {
                _data.emit(it!!.data ?: listOf())
            }
        }
    }
}