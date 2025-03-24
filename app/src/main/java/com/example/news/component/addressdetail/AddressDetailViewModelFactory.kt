package com.example.news.component.addressdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddressDetailViewModelFactory(private val id: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressDetailViewModel::class.java)) {
            return AddressDetailViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}