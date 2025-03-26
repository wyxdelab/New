package com.example.news.component.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.drake.channel.sendEvent
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.userdetail.UserDetailActivity
import com.example.news.databinding.ActivityUserBinding
import com.example.news.util.Constant
import com.example.news.util.SuperRecyclerViewUtil
import com.ixuea.courses.mymusic.component.user.UserViewModelFactory
import com.xm.letterindex.LetterIndexView
import kotlinx.coroutines.launch
import show

class UserActivity : BaseTitleActivity<ActivityUserBinding>() {

    private lateinit var viewModel: UserViewModel
    private lateinit var layoutManager: LinearLayoutManager
    private var adapter: UserAdapter = UserAdapter()

    override fun initViews() {
        super.initViews()
        SuperRecyclerViewUtil.initVerticalLinearRecyclerView(binding.list, true)
        layoutManager = binding.list.layoutManager as LinearLayoutManager
    }
    override fun initDatum() {
        super.initDatum()
        binding.list.adapter = adapter

        val viewModelFactory = UserViewModelFactory(extraInt(Constant.STYLE))
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.title
                .collect {
                    setTitle(it)
                }
        }

        lifecycleScope.launch {
            viewModel.data
                .collect {
                    adapter.submitList(it.datum)
                    binding.letterIndex.show()
                    binding.letterIndex.setLetterList(it.letters)
                }
        }

        viewModel.loadData()
    }

    override fun initListeners() {
        super.initListeners()
        binding.letterIndex.setOnStateChangeListener(LetterIndexView.OnStateChangeListener { eventAction, position, letter, itemCenterY ->
            layoutManager!!.scrollToPositionWithOffset(
                viewModel.userResult.indexes.get(position),
                0
            )
        })

        adapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.getItem(position)!!
            if (data is User) {
                if (Constant.STYLE_FRIEND_SELECT === viewModel.style) {
                    sendEvent(SelectedFriendEvent(data))

                    //关闭界面
                    finish()
                } else {
                    startActivityExtraId(UserDetailActivity::class.java, data.id!!)
                }
            }
        }
    }

    /**
     * 创建菜单方法
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //返回menu布局
        menuInflater.inflate(R.menu.search, menu)

        //查找搜索按钮
        val searchItem = menu.findItem(R.id.action_search)

        //在搜索按钮里面查询搜索控件
        val searchView = searchItem.actionView as SearchView?

        //在这里就可以配置SearchView

        //设置搜索监听器
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /**
             * 提交了搜索
             *
             * 在输入框中按回车
             * 点击了键盘上的搜索按钮
             * @param query
             * @return
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                onSearchTextChanged(query)
                return true
            }

            /**
             * 搜索框内容改变了
             * @param newText
             * @return
             */
            override fun onQueryTextChange(newText: String): Boolean {
                onSearchTextChanged(newText)
                return true
            }
        })

        //是否进入界面就打开搜索栏，false为默认打开，默认为true
//        if (Constant.STYLE_FRIEND_SELECT === style) {
//            searchView.isIconified = false
//        } else {
//            searchView.isIconified = true
//        }

        //搜索提示
        searchView.queryHint = getString(R.string.hint_search_user)
        return true
    }

    private fun onSearchTextChanged(query: String) {
        viewModel.filter(query)
    }


    companion object {
        /**
         * 启动界面
         *
         * @param context
         * @param style
         */
        fun start(context: Context, style: Int) {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(Constant.STYLE, style)
            context.startActivity(intent)
        }
    }
}