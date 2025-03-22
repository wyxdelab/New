package com.example.news.component.content

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.entity.response.Meta
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.handleCoroutineException
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

/**
 * 内容界面的viewModel
 */
class ContentViewModel(private val categoryId:String?): BaseViewModel() {
    var lastId: String? = null
    private val _data = MutableSharedFlow<Meta<Content>>()
    val data: Flow<Meta<Content>> = _data


    fun loadMore(lastId: String? = null) {
        this.lastId = lastId
        viewModelScope.launch {
            try {
                val r = DefaultNetworkRepository.contents(lastId, categoryId)
                if (r.isSucceeded) {
                    _data.emit(r.data!!)
                } else {
                    _response.value = r
                }
            } catch (e: Exception) {
                Log.d("TAG","error")
                _exception.value = e
            }

        }
    }

}