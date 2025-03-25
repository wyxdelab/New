package com.example.news.component.order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.news.adapter.BaseFragmentStateAdapter

class OrderPageAdapter(fragmentActivity: FragmentActivity) :
    BaseFragmentStateAdapter<Int>(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return OrderFragment.newInstance(getData(position))
    }
}