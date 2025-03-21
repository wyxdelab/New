package com.example.news.component.content

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.FragmentContentBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.Constant
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

/**
 * 内容界面
 */
class ContentFragment: BaseViewModelFragment<FragmentContentBinding>() {

    private lateinit var contentAdaptor: ContentAdaptor
    private lateinit var viewModel: ContentViewModel
    private var isFirstShow: Boolean = true

    override fun initViews() {
        super.initViews()
        binding.list.apply {
            layoutManager = LinearLayoutManager(hostActivity)
            val decoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            addItemDecoration(decoration)
        }
    }

    override fun initDatum() {
        super.initDatum()
        val viewModelFactory = ContentViewModelFactory(requireArguments().getString(Constant.ID))
        viewModel = ViewModelProvider(this, viewModelFactory).get(ContentViewModel::class.java)
        contentAdaptor = ContentAdaptor()
        binding.list.adapter = contentAdaptor
        lifecycleScope.launch {
            viewModel.data.collect{
                if (StringUtils.isBlank(viewModel.lastId)) {    //下拉刷新
                    contentAdaptor.submitList(it.data)
                } else { //上拉加载更多
                    it.data?.let {
                        contentAdaptor.addAll(it)
                    }

                }
                processRefreshAndLoadMoreStatus(true, it.data?.isEmpty() ?: true)
            }
        }

    }

    private fun processRefreshAndLoadMoreStatus(success: Boolean, noMore: Boolean = false) {
        //传入false表示刷新失败
        binding.refresh.finishRefresh(500, success, false)

        //next=null，表示没有更多数据了
        binding.refresh.finishLoadMore(500, success, noMore)
    }

    override fun initListeners() {
        super.initListeners()
        binding.refresh.setOnRefreshListener{ //下拉刷新监听
            viewModel.loadMore()
        }

        binding.refresh.setOnLoadMoreListener{//上拉加载更多监听
            viewModel.loadMore(contentAdaptor.items.lastOrNull()?.id)
        }
    }
    override fun onResume() {
        super.onResume()
        if (isFirstShow) {
            viewModel.loadMore()
            isFirstShow = false
        }

    }
    companion object {
        const val TAG = "ContentFragment"
        fun newInstance(categoryId: String? = null): ContentFragment{
            val args = Bundle()
            categoryId?.let {
                args.putString(Constant.ID,it)
            }
            val fragment = ContentFragment()
            fragment.arguments = args
            return fragment
        }
    }

}