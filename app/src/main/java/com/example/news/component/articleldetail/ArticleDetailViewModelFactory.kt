package com.example.news.component.articleldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ArticleDetailViewModelFactory(private val id: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticleDetailViewModel::class.java)) {
            return ArticleDetailViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}