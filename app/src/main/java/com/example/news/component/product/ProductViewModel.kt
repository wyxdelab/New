package com.example.news.component.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.news.model.BaseViewModel
import kotlinx.coroutines.flow.Flow
import org.apache.commons.lang3.StringUtils

class ProductViewModel : BaseViewModel() {
    /**
     * 打开详情界面
     */
    private val _detail = MutableLiveData<Product>()
    var detail = _detail

    /**
     * 当前搜索关键字
     */
    private var query: String? = null

    /**
     * 排序方法，取值查看ProductService.products方法注释
     */
    private var order = 0

    /**
     * 根据品牌搜索
     */
    private var brand: String? = null

    /**
     * 通过这个变量控制外界加载数据
     */
    private val _loadData = MutableLiveData<Long>()
    var loadData: LiveData<Long> = _loadData

    /**
     * 商品列表
     *
     * @return
     */
    fun products(): Flow<PagingData<Product>> {
        val r = productRepository.products(order, query, brand)
        return r
    }

    /**
     * 设置查询关键字
     *
     * @param query
     */
    fun setQuery(query: String?) {
        this.query = query
        setLoadData()
    }

    /**
     * 设置排序
     *
     * @param data
     */
    fun setSort(data: Int) {
        order = data
        setLoadData()
    }

    /**
     * 品牌筛选改变了
     *
     * @param data
     */
    fun setBrand(data: List<String>?) {
        if (data == null) {
            brand = null
        } else {
            //多个品牌拼接为空格传递，当然如果服务端需要列表，也可以直接传递列表
            brand = StringUtils.join(data, ",")
        }
        setLoadData()
    }

    private fun setLoadData() {
        _loadData.value = System.currentTimeMillis()
    }

    /**
     * 跳转到详情界面
     */
    fun detail(data: Product) {
        _detail.value = data
    }

    companion object {
        val productRepository = ProductRepository()
    }
}