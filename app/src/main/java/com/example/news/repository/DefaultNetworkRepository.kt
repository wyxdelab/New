package com.example.news.repository

import com.example.news.component.api.DefaultNetworkService
import com.example.news.component.content.Content
import com.example.news.entity.response.ListResponse
import retrofit2.http.Query

/**
 * 网络数据仓库
 */
object DefaultNetworkRepository {
    private val service: DefaultNetworkService by lazy {
        DefaultNetworkService.create()
    }

    suspend fun contents(
        last: String? = null,
        categoryId: String? = null,
        userId: String? = null,
        size: Int = 10,
        style: Int? = null
    ): ListResponse<Content> {
        return service.contents(last, userId, categoryId, size, style)
    }
}