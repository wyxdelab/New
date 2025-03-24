package com.example.news.component.product

import com.example.news.component.api.NetworkModule
import com.example.news.entity.response.ListResponse
import retrofit2.http.*

interface ProductNetworkService {
    /**
     * 商品列表
     *
     * @return
     */
    @GET("v1/products")
    suspend fun products(
        @Query(value = "page") page: Int,
        @Query(value = "size") size: Int,
        @Query(value = "order") order: Int,
        @Query(value = "query") query: String?,
        @Query(value = "brand") brand: String?,
    ): ListResponse<Product>

    companion object {
        private var instance: ProductNetworkService? = null

        fun create(): ProductNetworkService {
            if (instance == null) {
                instance = NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient())
                    .create(ProductNetworkService::class.java)
            }
            return instance!!
        }
    }
}