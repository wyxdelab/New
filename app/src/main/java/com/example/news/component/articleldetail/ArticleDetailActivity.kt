package com.example.news.component.articleldetail

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.QuickAdapterHelper
import com.example.news.R
import com.example.news.activity.BaseViewModelActivity
import com.example.news.component.content.Content
import com.example.news.component.userdetail.UserDetailActivity
import com.example.news.databinding.ActivityArticleDetailBinding
import com.example.news.util.Constant
import com.example.news.util.ImageUtil
import kotlinx.coroutines.launch

/**
 * 文章详情界面
 */
class ArticleDetailActivity : BaseViewModelActivity<ActivityArticleDetailBinding>() {

    private lateinit var articleDetailHeaderAdapter: ArticleDetailHeaderAdapter
    private lateinit var helper: QuickAdapterHelper
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var articleDetailAdaptor: ArticleDetailAdaptor
    private lateinit var viewModel: ArticleDetailViewModel

    override fun initViews() {
        super.initViews()
        layoutManager = LinearLayoutManager(this)
        binding.list.layoutManager = layoutManager
    }

    override fun initDatum() {
        super.initDatum()
        val viewModelFactory =
            ArticleDetailViewModelFactory(intent.getStringExtra(Constant.ID)!!)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ArticleDetailViewModel::class.java)
        initViewModel(viewModel)
        //监听文章数据
        lifecycleScope.launch {
            viewModel.data.collect {
                showData(it)
            }
        }
        articleDetailAdaptor = ArticleDetailAdaptor(viewModel)
        helper = QuickAdapterHelper.Builder(articleDetailAdaptor).build()
        articleDetailHeaderAdapter = ArticleDetailHeaderAdapter(viewModel).apply {
            //item子控件点击
            addOnItemChildClickListener(R.id.user_container) { adapter, _, position ->
                startActivityExtraId(UserDetailActivity::class.java, viewModel.content.user!!.id!!)
            }


        }

        helper.addBeforeAdapter(articleDetailHeaderAdapter)
        binding.list.adapter = helper.adapter
        //监听评论数据
        lifecycleScope.launch {
            viewModel.comments
                .collect { data ->
                    if (data.page == 1) {
                        articleDetailAdaptor.submitList(data.data)
                    } else {
                        articleDetailAdaptor.addAll(data.data!!)
                    }
                }
        }

        //获取数据
        viewModel.loadData()
    }

    private fun showData(data: Content) {
        articleDetailHeaderAdapter.setItem(data, null)

        //用户信息
        ImageUtil.showAvatar(binding.icon, data.user!!.icon)
        binding.nickname.text = data.user!!.nickname

        if (data.isLike()) {
            binding.like.setImageResource(R.drawable.thumb_selected)
        } else {
            binding.like.setImageResource(R.drawable.baseline_thumb)
        }
    }

    override fun initListeners() {
        super.initListeners()
        //返回按钮点击
        binding.back.setOnClickListener {
            finish()
        }

        //监听列表滚动，在顶部显示用户头像
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 获取第一个可见的item的position
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()

                // 获取RecyclerView的滚动距离
                val scrollY = recyclerView.computeVerticalScrollOffset()

                Log.d("TAG", "onScrolled: $scrollY")

                binding.userContainer.visibility =
                    if (scrollY >= 350) View.VISIBLE else View.INVISIBLE
            }
        })
    }

    companion object {
        const val TAG = "ArticleDetailActivity"
    }


}