package com.ixuea.courses.mymusic.component.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.component.user.UserViewModel

class UserViewModelFactory(private val style: Int) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(style) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}