package com.example.news.component.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.chad.library.adapter.base.QuickAdapterHelper
import com.drake.channel.sendEvent
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.activity.BaseViewModelActivity
import com.example.news.databinding.ActivitySearchBinding
import com.example.news.databinding.HeaderSearchBinding
import com.example.news.databinding.ItemTagBinding
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.SuperRecyclerViewUtil
import com.example.superui.util.SuperKeyboardUtil
import com.example.superui.util.SuperTabUtil
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import show

class SearchActivity : BaseTitleActivity<ActivitySearchBinding>() {

    private var suggestionRunnable: Runnable? = null
    private var data: String? = null
    private lateinit var searchView: SearchView
    private lateinit var searchCategoryAdapter: SearchCategoryAdapter
    private lateinit var searchHeaderAdapter: SearchHeaderAdapter
    private lateinit var headerSearchBinding: HeaderSearchBinding
    private lateinit var searchHistoryAdapterHelper: QuickAdapterHelper
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchAutoComplete: SearchView.SearchAutoComplete

    /**
     * 搜索建议适配器
     */
    private lateinit var suggestAdapter: ArrayAdapter<String>

    protected override fun initViews() {
        super.initViews()
        SuperRecyclerViewUtil.initVerticalLinearRecyclerView(binding.list)
        SuperTabUtil.bind(binding.indicator, binding.pager)
    }

    protected override fun initDatum() {
        super.initDatum()
        //搜索历史适配器
        searchHistoryAdapter = SearchHistoryAdapter()

        searchHistoryAdapterHelper = QuickAdapterHelper.Builder(searchHistoryAdapter).build()

        //头部
        headerSearchBinding = HeaderSearchBinding.inflate(layoutInflater, binding.list, false)
        searchHeaderAdapter = SearchHeaderAdapter(headerSearchBinding)
        searchHistoryAdapterHelper.addBeforeAdapter(searchHeaderAdapter)

        binding.list.adapter = searchHistoryAdapterHelper.adapter

        //搜索结果适配器
        searchCategoryAdapter = SearchCategoryAdapter(hostActivity)
        binding.pager.adapter = searchCategoryAdapter

        //创建tab
        binding.indicator.removeAllTabs()
        for (title in indicatorTitles) {
            binding.indicator.addTab(binding.indicator.newTab().setText(title))
        }
        searchCategoryAdapter.setDatum(indicatorTitles)
        binding.pager.offscreenPageLimit = indicatorTitles.size

        binding.pager.post {
            loadPopularData()
            loadSearchHistoryData()
        }
    }

    private fun loadPopularData() {
        val datum: MutableList<String> = ArrayList()
        datum.add("爱学啊")
        datum.add("我的云音乐")
        datum.add("Android项目实战")
        datum.add("人生苦短")
        datum.add("我们只做好课")
        datum.add("android开发")
        datum.add("项目课程")

        //设置热门搜索数据
        setPopularData(datum)
    }

    private fun setPopularData(datum: List<String>) {
        for (data in datum) {
            //循环每一个数据

            //创建布局
            val itemTagBinding = ItemTagBinding.inflate(layoutInflater, binding.list, false)

            //设置数据
            itemTagBinding.title.text = data

            //设置点击事件
            itemTagBinding.root.setOnClickListener { setSearchData(data) }

            //添加控件
            headerSearchBinding.floatLayout.addView(itemTagBinding.root)
        }
    }

    private fun loadSearchHistoryData() {
        searchHistoryAdapter.submitList(getOrm().querySearchHistory())
        refreshSearchHistoryTitleStatus()
    }

    private fun refreshSearchHistoryTitleStatus() {
        headerSearchBinding.searchHistoryTitle.show(searchHistoryAdapter.itemCount > 0)
    }

    private fun setSearchData(data: String) {
        //将内容设置到搜索控件
        //并马上执行搜索
        searchView.setQuery(data, true)

        //进入搜索状态
        searchView.isIconified = false

        //隐藏软键盘
        SuperKeyboardUtil.hideKeyboard(hostActivity)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search, menu)

        //查找搜索按钮
        val searchItem = menu.findItem(R.id.action_search)

        //查找搜索控件
        searchView = searchItem.actionView as SearchView

        //可以在这里配置SearchView

        //设置搜索监听器
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /**
             * 提交了搜索
             * 回车搜索调用两次
             * 点击键盘上搜索
             * @param query
             * @return
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                data = query
                performSearch()
                return true
            }

            /**
             * 搜索输入框文本改变了
             * @param newText
             * @return
             */
            override fun onQueryTextChange(newText: String): Boolean {
                prepareLoadSuggestion(newText)
                return true
            }
        })

        //是否进入界面就打开搜索栏
        //false为默认打开
        //默认为true
        searchView.isIconified = false

        //设置关闭监听器
        searchView.setOnCloseListener {

            //显示搜索历史控件
            showSearchHistoryView()
            false
        }

        //查找搜索建议控件
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text)

        //默认要输入两个字符才显示提示，可以这样更改
        searchAutoComplete.setThreshold(1)

        //获取搜索管理器
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        //设置搜索信息
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        //设置搜索建议点击回调
        searchAutoComplete.setOnItemClickListener { parent, view, position, id ->
            setSearchData(
                suggestAdapter.getItem(position)!!
            )
        }
        return true
    }

    private fun prepareLoadSuggestion(data: String) {
        if (StringUtils.isBlank(data)) {
            return
        }

        //取消原来的任务
        if (suggestionRunnable != null) {
            binding.pager.removeCallbacks(suggestionRunnable)
            suggestionRunnable = null
        }

        //创建新的任务
        suggestionRunnable = Runnable { loadSuggestion(data) }

        //500毫秒后执行
        binding.pager.postDelayed(suggestionRunnable, 500)
    }

    private fun loadSuggestion(data: String) {
        lifecycleScope.launch {
            val r = DefaultNetworkRepository.searchSuggest(data)
            setSuggest(r.data!!)
        }
    }

    private fun setSuggest(data: Suggest) {
        //处理搜索建议数据

        //像变换这个中操作
        //如果是Kotlin语言中就一句话的事
        val datum: MutableList<String> = java.util.ArrayList()

        //处理内容搜索建议
        if (data.contents != null) {
            for (title in data.contents) {
                datum.add(title.title!!)
            }
        }

        //处理用户搜索建议
        if (data.users != null) {
            for (title in data.users) {
                datum.add(title.title!!)
            }
        }

        //创建适配器
        suggestAdapter = ArrayAdapter<String>(
            hostActivity,
            R.layout.item_suggest,
            R.id.title,
            datum
        )

        //设置到控件
        searchAutoComplete.setAdapter<ArrayAdapter<String>>(suggestAdapter)
    }

    /**
     * 显示搜索历史控件
     */
    private fun showSearchHistoryView() {
        binding.searchResultContainer.visibility = View.GONE
        binding.list.visibility = View.VISIBLE
    }

    /**
     * 执行搜索
     */
    private fun performSearch() {
        if (StringUtils.isEmpty(data)) {
            //没有数据直接返回
            return
        }

        //保存搜索历史
        val searchHistory = SearchHistory()
        searchHistory.content = data
        searchHistory.createdAt = System.currentTimeMillis()
        getOrm().createOrUpdate(searchHistory)

        //显示搜索结果控件
        showSearchResultView()

        //发布搜索Key
        sendEvent(SearchEvent(data!!, binding.pager.currentItem))
        loadSearchHistoryData()
    }

    /**
     * 显示搜索结果控件
     */
    private fun showSearchResultView() {
        binding.searchResultContainer.visibility = View.VISIBLE
        binding.list.visibility = View.GONE
    }

    protected override fun initListeners() {
        super.initListeners()
        searchHistoryAdapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.getItem(position) as SearchHistory
            setSearchData(data.content!!)
        }

        searchHistoryAdapter.addOnItemChildClickListener(R.id.delete) { adapter, view, position ->
            val data = adapter.getItem(position) as SearchHistory
            getOrm().deleteSearchHistory(data)
            adapter.removeAt(position)
            refreshSearchHistoryTitleStatus()
        }

        //pager滚动监听器，目的是实现滚动的时候，发送当前页搜索事件
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //执行搜索
                performSearch()
            }
        })
    }
    /**
     * 物理按键返回调用
     */
    override fun onBackPressed() {
        if (searchView.isIconified) {
            //不是在搜索状态

            //正常返回
            super.onBackPressed()
        } else {
            //是搜索状态

            //关闭搜索状态
            searchView!!.isIconified = true

            showSearchHistoryView()
        }
    }
    companion object {
        /**
         * 标题数据
         */
        private val indicatorTitles = listOf(
            R.string.article,
            R.string.user,
            R.string.video,
            R.string.video,
            R.string.video,
            R.string.video,
            R.string.video,
            R.string.video,
            R.string.video,
            R.string.video,
            R.string.video,
            R.string.video
        )
    }
}