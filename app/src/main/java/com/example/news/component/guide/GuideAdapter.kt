package com.example.news.component.guide

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.news.adapter.BaseFragmentPagerAdapter

/**
 * 引导界面适配器
 */
class GuideAdapter(context: Context, fragmentManager: FragmentManager): BaseFragmentPagerAdapter<Int>(context, fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return GuideFragment.newInstance(getData(position))
    }

}