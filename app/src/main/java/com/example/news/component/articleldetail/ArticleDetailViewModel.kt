package com.example.news.component.articleldetail

import androidx.lifecycle.viewModelScope
import com.example.news.component.comment.Comment
import com.example.news.component.content.Content
import com.example.news.entity.response.Meta
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class ArticleDetailViewModel(private val id: String) : BaseViewModel() {
    private val _data = MutableSharedFlow<Content>()
    val data: Flow<Content> = _data

    private val _comments = MutableSharedFlow<Meta<Comment>>()
    val comments: Flow<Meta<Comment>> = _comments

    lateinit var content: Content
    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.contentDetail(id)
                .onSuccess(viewModel) {
                    content = it!!
                    _data.emit(it!!)
                }
        }

        loadMoreComment()

    }

    private fun loadMoreComment() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.comments(articleId = id)
                .onSuccess(viewModel) {
                    _comments.emit(it!!)
                }
        }
    }

    fun loadReplyComment(data: Comment) {

    }

    fun jumpToUserDetail(userName: String) {
        //需要通过服务端查找到用户id，从而跳转至对应的用户详情界面
    }


}