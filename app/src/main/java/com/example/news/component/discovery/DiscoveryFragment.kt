package com.example.news.component.discovery

import android.content.Intent
import android.os.Bundle
import com.example.news.adapter.TabLayoutViewPager2Mediator
import com.example.news.component.main.MainActivity
import com.example.news.component.publish.PublishActivity
import com.example.news.component.search.SearchActivity
import com.example.news.databinding.FragmentDiscoveryBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.util.DataUtil

class DiscoveryFragment: BaseViewModelFragment<FragmentDiscoveryBinding>() {

    override fun initDatum() {
        super.initDatum()
        binding.apply {
            pager.adapter = DiscoveryAdaptor(requireActivity(), DataUtil.categories)
            TabLayoutViewPager2Mediator(indicator, pager) {
                indicator, pager ->
            }.attach()
        }
    }


    override fun initListeners() {
        super.initListeners()
        binding.menu.setOnClickListener {
            (hostActivity as MainActivity).openDrawer()
        }

        binding.add.setOnClickListener {
            loginAfter {
                startActivity(Intent(requireActivity(), PublishActivity::class.java))
            }
        }

        binding.search.setOnClickListener {
            startActivity(SearchActivity::class.java)
        }

    }
    companion object {
        fun newInstance(): DiscoveryFragment {
            val args = Bundle()

            val fragment = DiscoveryFragment()
            fragment.arguments = args
            return fragment
        }
    }

}