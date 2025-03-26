package com.example.news.component.category

import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CategoryViewModel() : BaseViewModel() {
    private val _leftData = MutableSharedFlow<List<Category>>()
    val leftData: Flow<List<Category>> = _leftData

    private val _rightData = MutableSharedFlow<List<Category>>()
    val rightData: Flow<List<Category>> = _rightData

    fun loadLeftData() {
//        if (NetworkUtils.isAvailableByPing()) {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.categories().onSuccess(viewModel) {
                _leftData.emit(it!!.data!!)
            }
        }
//        }
    }

    fun loadRightData(id: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.categories(id).onSuccess(viewModel) {
                _rightData.emit(it!!.data ?: listOf())
            }
        }
    }
}