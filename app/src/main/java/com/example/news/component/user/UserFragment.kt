package com.example.news.component.user

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.news.R
import com.example.news.component.search.BaseSearchFragment
import com.example.news.component.userdetail.UserDetailActivity
import com.example.news.databinding.ListBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.Constant
import com.example.news.util.SuperRecyclerViewUtil
import hide
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils
import show

/**
 * 用户搜索结果界面
 */
class UserFragment : BaseSearchFragment() {
    private lateinit var adapter: UserAdapter
    protected override fun initViews() {
        super.initViews()
        SuperRecyclerViewUtil.initVerticalLinearRecyclerView(binding.list)
    }
    protected override fun initDatum() {
        super.initDatum()
        adapter = UserAdapter()
        binding.list.adapter = adapter
    }
    override fun loadData() {
        super.loadData()
        lifecycleScope.launch {
            val r = DefaultNetworkRepository.searchUser(query!!)
            if (CollectionUtils.isEmpty(r.data!!.data)) {
                binding.placeholderView.show()
                binding.placeholderView.showTitle(R.string.no_data)
                binding.list.hide()
            } else {
                binding.placeholderView.hide()
                binding.list.show()
                adapter.submitList(r.data!!.data)
            }
        }
    }

    protected override fun initListeners() {
        super.initListeners()
        adapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.getItem(position) as User
            startActivityExtraId(UserDetailActivity::class.java, data.id!!)
        }
    }

    companion object {
        fun newInstance(position: Int): UserFragment {
            val args = Bundle()
            args.putInt(Constant.STYLE, position)

            val fragment = UserFragment()
            fragment.arguments = args
            return fragment
        }
    }
}