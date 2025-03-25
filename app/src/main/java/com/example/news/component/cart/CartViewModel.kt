package com.example.news.component.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CartViewModel : BaseViewModel() {
    /**
     * 删除购物车成功
     */
    private val _deleteSuccess = MutableLiveData<Long>()
    var deleteSuccess: LiveData<Long> = _deleteSuccess

    /**
     * 编辑成功
     */
    private val _editSuccess = MutableLiveData<Long>()
    var editSuccess: LiveData<Long> = _editSuccess

    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.carts().onSuccess(viewModel) {
                _data.emit(it!!.data ?: listOf())
            }
        }
    }

    /**
     * 删除购物车（项）
     *
     * @return
     */
    fun deleteCarts(data: ArrayList<String>) {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.deleteCarts(data).onSuccess(viewModel) {
                _deleteSuccess.value = System.currentTimeMillis()
            }
        }
    }

    fun editCart(data: Cart) {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.editCart(data).onSuccess(viewModel) {
                _editSuccess.value = System.currentTimeMillis()
            }
        }
    }

    private val _data = MutableSharedFlow<List<Cart>>()
    val data: Flow<List<Cart>> = _data


}