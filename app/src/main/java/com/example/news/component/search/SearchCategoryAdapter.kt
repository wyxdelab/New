package com.example.news.component.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.news.adapter.BaseFragmentStateAdapter
import com.example.news.component.content.ContentFragment
import com.example.news.component.user.UserFragment
import com.example.news.util.Constant

/**
 * 搜索结果分类适配器
 */
class SearchCategoryAdapter(fragmentActivity: FragmentActivity?) : BaseFragmentStateAdapter<Int>(
    fragmentActivity!!
) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            //文章搜索结果
            ContentFragment.newInstance()
        } else if (position == 1) {
            //用户搜索结果
            UserFragment.newInstance(position)
        } else {
            //TODO 其他搜索结果
            OtherFragment.newInstance()
        }
    }
}