package com.example.news.component.address

import androidx.lifecycle.viewModelScope
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class AddressViewModel (val style: Int) : BaseViewModel() {
    private val _data = MutableSharedFlow<List<Address>>()
    val data: Flow<List<Address>> = _data

    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.addresses().onSuccess(viewModel) {
                _data.emit(it?.data ?: listOf())
            }
        }
    }
}