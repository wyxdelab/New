package com.example.news.component.orderdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrderDetailViewModelFactory(private val id: String, private val style: Int?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            return OrderDetailViewModel(id, style) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}