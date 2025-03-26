package com.example.news.component.content

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.entity.response.Meta
import com.example.news.entity.response.onSuccess
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
class ContentViewModel(private val categoryId: String?, val index: Int) : BaseViewModel() {
    var lastId: String? = null

    var query: String? = null
    private val _data = MutableSharedFlow<Meta<Content>>()
    val data: Flow<Meta<Content>> = _data

    private val _toArticleDetail = MutableSharedFlow<String>()
    val toArticleDetail: SharedFlow<String> = _toArticleDetail

    private val _toCourseDetail = MutableSharedFlow<String>()
    val toCourseDetail: SharedFlow<String> = _toCourseDetail


    fun loadMore(lastId: String? = null) {
        this.lastId = lastId

        if (index == 0) {
            viewModelScope.launch(coroutineExceptionHandler) {

                DefaultNetworkRepository.searchContent(query!!).onSuccess(viewModel) {
                    _data.emit(it!!)
                }
            }
        } else {
            viewModelScope.launch(coroutineExceptionHandler) {

                DefaultNetworkRepository.contents(lastId, categoryId).onSuccess(viewModel) {
                    _data.emit(it!!)
                }


            }
        }


    }

    /**
     * 列表item点击
     */
    fun itemClick(data: Content) {
        viewModelScope.launch {
            if (StringUtils.isNotBlank(data.uri)) {
                _toCourseDetail.emit(data.id!!)
            } else {
                _toArticleDetail.emit(data.id!!)
            }
        }
    }
}