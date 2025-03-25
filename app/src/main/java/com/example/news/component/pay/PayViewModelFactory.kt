package com.ixuea.courses.mymusic.component.pay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.component.pay.PayViewModel

class PayViewModelFactory(private val style: Int, private val id: String) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PayViewModel::class.java)) {
            return PayViewModel(style, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}