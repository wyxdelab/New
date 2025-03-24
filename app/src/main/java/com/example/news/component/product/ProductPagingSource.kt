package com.example.news.component.product

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.news.exception.EmptyException

class ProductPagingSource(
    private val order: Int,
    private val query: String?,
    private val brand: String?
) : PagingSource<Int, Product>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Product> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
//            val nextPageNumber = 100
            val response =
                ProductNetworkService.create().products(nextPageNumber, 10, order, query, brand)

            return if (response.isSucceeded) {
                LoadResult.Page(
                    data = response.data?.data ?: listOf(),
                    prevKey = null, // Only paging forward.
                    nextKey = response.data!!.next
                )
            } else {
                LoadResult.Error(EmptyException())
            }
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return null
    }
}