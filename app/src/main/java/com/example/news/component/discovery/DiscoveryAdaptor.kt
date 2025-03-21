package com.example.news.component.discovery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.news.component.category.Category
import com.example.news.component.content.ContentFragment

/**
 * 发现界面adaptor
 */
class DiscoveryAdaptor(fragmentActivity: FragmentActivity, private val datum: List<Category>): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return datum.size
    }

    override fun createFragment(position: Int): Fragment {
        return ContentFragment.newInstance(datum[position].id)
    }
}