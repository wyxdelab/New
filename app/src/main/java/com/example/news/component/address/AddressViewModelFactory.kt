package com.example.news.component.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddressViewModelFactory(private val style: Int) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddressViewModel::class.java)) {
            return AddressViewModel(style) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}