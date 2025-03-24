package com.example.news.component.product

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.databinding.ActivityProductBinding
import com.example.news.databinding.DropdownPhoneBrandBinding
import com.example.news.entity.response.BaseResponse
import com.example.news.util.Constant
import com.example.news.util.SuperRecyclerViewUtil
import com.example.superui.dropdown.DropMenuItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import show

/**
 * 商城界面
 */
class ProductActivity : BaseTitleActivity<ActivityProductBinding>() {
    private val dropdownMenuContentViews = mutableListOf<View>()
    private lateinit var searchView: SearchView
    private var changeStyleMenuItem: MenuItem? = null
    private var isScrollTop: Boolean = false
    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: ProductViewModel
    private lateinit var layoutManager: GridLayoutManager

    override fun initViews() {
        super.initViews()

        binding.list.setHasFixedSize(true)
        layoutManager = GridLayoutManager(hostActivity, 1)
        binding.list.layoutManager = layoutManager

        //刷新箭头颜色
        binding.refresh.setColorSchemeResources(R.color.primary)

        binding.refresh.setProgressBackgroundColorSchemeResource(R.color.white)

        //添加下拉菜单
        addListPopupMenuView(
            listOf<DropMenuItem>(
                DropMenuItem(R.string.comprehensive, Constant.VALUE0),
                DropMenuItem(R.string.price_desc, Constant.VALUE10),
                DropMenuItem(R.string.price_asc, Constant.VALUE20),
                DropMenuItem(R.string.id_asc, Constant.VALUE30)
            )
        )

        addGridPopupMenuView(resources.getStringArray(R.array.phone_brand).toList())

        binding.dropdown.setupDropDownMenu(
            listOf(
                getString(R.string.comprehensive),
                getString(R.string.brand)
            ),
            dropdownMenuContentViews
        )
    }

    override fun initDatum() {
        super.initDatum()
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        adapter = ProductAdapter(viewModel)
        binding.list.adapter = adapter

        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.products().collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.refresh.setOnRefreshListener { adapter.refresh() }

        //监听适配器加载状态，包括错误处理
        //主要是实现，筛选，排序，搜索更改后，列表滚动到顶部
        //https://developer.android.google.cn/topic/libraries/architecture/paging/load-state#java
        adapter.addLoadStateListener { state ->
            //是否加载中
            val isLoading = state.refresh is LoadState.Loading
            binding.refresh.isRefreshing = isLoading

            //处理空列表
            val isEmpty = state.refresh is LoadState.NotLoading && adapter.itemCount === 0
            showEmpty(isEmpty)

            //处理错误状态
            val isError = state.refresh is LoadState.Error
            if (isError) {
                binding.placeholderView.show()
                var errorData: BaseResponse? = null
                var error: Throwable? = null
                val loadStateError = state.refresh as LoadState.Error
                showError(loadStateError.error)
            }
            if (isScrollTop) {
                binding.list.postDelayed({ binding.list.scrollToPosition(0) }, 300)
                isScrollTop = false
            }
            null
        }

        binding.placeholderView.setOnClickListener {
            adapter.retry()
        }

        viewModel.loadData.observe(this) { data -> loadData() }
    }
    private fun showError(error: Throwable) {
        binding.placeholderView.show(true)
        binding.list.show(false)
        binding.placeholderView.showTitle(R.string.error_loading)
    }
    private fun showEmpty(data: Boolean) {
        binding.placeholderView.show(data)
        binding.list.show(!data)
        binding.placeholderView.showTitle(R.string.no_result)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_product, menu)
        changeStyleMenuItem = menu.findItem(R.id.change_style)

        //查找搜索按钮
        val searchItem = menu.findItem(R.id.search)

        //查找搜索控件
        searchView = searchItem.actionView as SearchView

        //可以在这里配置SearchView

        //设置搜索监听器
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /**
             * 提交了搜索
             * 回车搜索调用两次
             * 点击键盘上搜索
             *
             * @param query
             * @return
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                setScrollTop()
                viewModel.setQuery(query)
                return true
            }

            /**
             * 搜索输入框文本改变了
             *
             * @param newText
             * @return
             */
            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        //设置关闭监听器
        searchView.setOnCloseListener(SearchView.OnCloseListener {
            setScrollTop()

            //显示搜索历史控件
            viewModel.setQuery(null)
            false
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun setScrollTop() {
        isScrollTop = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.change_style -> changeListStyle()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun changeListStyle() {
        if (layoutManager.spanCount == 2) {
            adapter.isListStyle = true
            layoutManager.spanCount = 1
//            changeStyleMenuItem.setIcon(R.drawable.style_grid)
        } else {
            adapter.isListStyle = false
            layoutManager.spanCount = 2
//            changeStyleMenuItem.setIcon(R.drawable.style_list)
        }
    }

    /**
     * 添加单选列表下拉菜单
     *
     * @param menuData
     */
    private fun addListPopupMenuView(menuData: List<DropMenuItem>) {
        val recyclerView = RecyclerView(hostActivity)
        recyclerView.setBackgroundResource(R.drawable.shape_surface)
        SuperRecyclerViewUtil.initVerticalLinearRecyclerView(recyclerView)

        val menuAdapter = DropDownListMenuAdapter()
        menuAdapter.setOnItemClickListener { adapter, view, position ->
            binding.dropdown.closeMenu()
            val menuItem: DropMenuItem = adapter.getItem(position) as DropMenuItem

            //设置当前tab标题
            binding.dropdown.setTabText(menuItem.title, 0)

            //设置当前位置选中
            (adapter as DropDownListMenuAdapter).setSelect(position)
            setScrollTop()
            viewModel.setSort(menuItem.value)
        }
        recyclerView.adapter = menuAdapter
        dropdownMenuContentViews.add(recyclerView)
        menuAdapter.submitList(menuData)
    }

    private fun addGridPopupMenuView(menuData: List<String>) {
        val phoneBandBinding: DropdownPhoneBrandBinding = DropdownPhoneBrandBinding.inflate(
            layoutInflater, null, false
        )
        phoneBandBinding.list.setHasFixedSize(true)

        //布局管理器
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(hostActivity, 2)
        phoneBandBinding.list.layoutManager = layoutManager

        val menuAdapter = DropDownGridMenuAdapter()
        menuAdapter.setOnItemClickListener { adapter, view, position ->
            (adapter as DropDownGridMenuAdapter).setSelect(
                position
            )
        }
        phoneBandBinding.list.adapter = menuAdapter

        //重置按钮点击
        phoneBandBinding.reset.setOnClickListener { v ->
            binding.dropdown.closeMenu()
            menuAdapter.resetSelect()

            //设置当前tab标题
            binding.dropdown.setTabText(R.string.brand, 1)
            setScrollTop()
            viewModel.setBrand(null)
        }

        //确定点击
        phoneBandBinding.confirm.setOnClickListener { v ->
            binding.dropdown.closeMenu()
            val selects = menuAdapter.select

            val title: String = if (selects.size > 1) {
                String.format("%s...", selects[0])
            } else if (selects.isNotEmpty()) {
                selects[0]
            } else {
                getString(R.string.brand)
            }

            //设置当前tab标题
            binding.dropdown.setTabText(title, 1)
            setScrollTop()
            viewModel.setBrand(selects)
        }
        menuAdapter.submitList(menuData)
        dropdownMenuContentViews.add(phoneBandBinding.getRoot())
    }
}