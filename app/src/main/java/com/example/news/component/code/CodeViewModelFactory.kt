package com.example.news.component.code

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CodeViewModelFactory(private val id: String) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CodeViewModel::class.java)) {
            return CodeViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}