package com.example.news.component.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InputIdentityViewModelFactory(private val style: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InputIdentityViewModel::class.java)) {
            return InputIdentityViewModel(style) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}