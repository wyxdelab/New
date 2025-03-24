package com.example.news.component.confirmorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ConfirmOrderViewModelFactory(private val id: String?, private val carts: ArrayList<String>?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfirmOrderViewModel::class.java)) {
            return ConfirmOrderViewModel(id, carts) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}