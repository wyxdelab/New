package com.example.news.component.code

import androidx.lifecycle.viewModelScope
import com.example.news.component.user.User
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class CodeViewModel(val id: String) : BaseViewModel() {
    private val _data = MutableSharedFlow<User>()
    val data: Flow<User> = _data

    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.userDetail(id).onSuccess(viewModel) {
                _data.emit(it!!)
            }
        }
    }
}