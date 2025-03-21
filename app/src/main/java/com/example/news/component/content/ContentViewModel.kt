package com.example.news.component.content

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.entity.response.Meta
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * 内容界面的viewModel
 */
class ContentViewModel: ViewModel() {
    private val _data = MutableSharedFlow<Meta<Content>>()
    val data: Flow<Meta<Content>> = _data
    fun loadMore(lastId: String? = null) {
        viewModelScope.launch {
            val r = DefaultNetworkRepository.contents(lastId)
            _data.emit(r.data!!)
        }
    }
}