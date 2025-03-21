package com.example.news.component.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.news.adapter.BaseFragmentPagerAdapter
import com.example.news.component.category.CategoryFragment
import com.example.news.component.home.HomeFragment
import com.example.news.component.me.MeFragment
import com.example.news.component.shortvideo.ShortVideoFragment

/**
 * 首页界面Adapter
 */
class MainAdaptor(fragmentActivity: FragmentActivity, private val count: Int): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            1 -> ShortVideoFragment.newInstance()
            2 -> CategoryFragment.newInstance()
            3 -> MeFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }
}