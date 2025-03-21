package com.example.news.component.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 内容ViewModel的工厂类
 */
class ContentViewModelFactory(private val categoryId: String?) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            return ContentViewModel(categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}