package com.example.news.component.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 内容ViewModel的工厂类
 */
class ContentViewModelFactory(private val categoryId: String?, val index: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentViewModel::class.java)) {
            return ContentViewModel(categoryId, index) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}