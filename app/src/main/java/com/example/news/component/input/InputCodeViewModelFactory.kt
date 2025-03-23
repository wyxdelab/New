package com.example.news.component.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InputCodeViewModelFactory(private val pageData: InputCodePageData) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InputCodeViewModel::class.java)) {
            return InputCodeViewModel(pageData) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}