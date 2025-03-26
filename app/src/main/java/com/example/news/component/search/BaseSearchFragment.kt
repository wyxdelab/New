package com.example.news.component.search

import com.drake.channel.receiveEvent
import com.example.news.databinding.ListBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.util.Constant

/**
 * 通用搜索fragment
 */
open class BaseSearchFragment : BaseViewModelFragment<ListBinding>() {
    protected var query: String? = null
    private var index = 0

    override fun initDatum() {
        super.initDatum()
        index = extraInt(Constant.STYLE)

        receiveEvent<SearchEvent> {
            searchEvent(it)
        }
    }

    /**
     * 搜索事件
     *
     * @param event
     */
    private fun searchEvent(event: SearchEvent) {
        query = event.data
        if (event.selectedIndex == index) {
            //只有索引一样才搜索
            //这样可以避免同时搜索多个界面
            loadData()
        }
    }

    open fun loadData() {

    }
}