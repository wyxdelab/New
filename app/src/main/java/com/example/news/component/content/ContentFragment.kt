package com.example.news.component.content

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drake.channel.receiveEvent
import com.example.news.component.articleldetail.ArticleDetailActivity
import com.example.news.component.publish.ContentChangedEvent
import com.example.news.databinding.FragmentContentBinding
import com.example.news.fragment.BaseViewModelFragment
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
        //显示数据
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
        //本地提示
        initViewModel(viewModel)
        contentAdaptor = ContentAdaptor(viewModel)
        binding.list.adapter = contentAdaptor
        //接收数据
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
        //接收到文章界面id，跳转至文章详情界面
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 在组件处于 STARTED 状态时执行协程任务
                viewModel.toArticleDetail.collect { id ->
                    val intent = Intent(requireContext(), ArticleDetailActivity::class.java)
                    intent.putExtra(Constant.ID, id)
                    startActivity(intent)
                }
            }
        }

        //监听发布的新文章
        receiveEvent<ContentChangedEvent> {
            viewModel.loadMore()
        }

    }

    private fun processRefreshAndLoadMoreStatus(success: Boolean, noMore: Boolean = false) {
        //传入false表示刷新失败
        binding.refresh.finishRefresh(500, success, false)

        //next=null，表示没有更多数据了
        binding.refresh.finishLoadMore(500, success, noMore)
    }

    override fun onError() {
        super.onError()
        processRefreshAndLoadMoreStatus(false)
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