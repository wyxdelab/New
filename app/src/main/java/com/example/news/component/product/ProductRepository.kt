package com.example.news.component.product

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.news.util.Constant

class ProductRepository {
    fun products(
        order: Int,
        query: String?,
        brand: String?,
    ) = Pager(
        PagingConfig(
            //每页加载数量
            Constant.DEFAULT_SIZE,

            //距离底部（下拉就是顶部）多少条数据时，开始加载更多，如果设置为0，表示要滑动到底部（顶部）才加载
            //默认为 每页加载数量，例如每页加载10条，这个参数也设置为10，表示滚动到还剩10条数据没显示时就开始加载更多
            //如果用户网络比较慢，还想用户无感知，那可以设置大一点，例如：5 * 每页加载数量
            Constant.DEFAULT_SIZE,

            false,

            //初始化加载多少条，默认为 每页加载数量*3
            Constant.DEFAULT_SIZE * 3
        )
    ) {
        ProductPagingSource(
            order,
            query,
            brand,
        )
    }.flow
}