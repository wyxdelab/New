package com.example.news.component.main

import com.angcyo.tablayout.ViewPagerDelegate
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.example.news.R
import com.example.news.activity.BaseViewModelActivity
import com.example.news.component.login.LoginHomeActivity
import com.example.news.databinding.ActivityMainBinding
import com.example.news.databinding.ItemTabBinding
import com.example.news.util.Constant

/**
 * 首页
 */
class MainActivity : BaseViewModelActivity<ActivityMainBinding>() {
    override fun initDatum() {
        super.initDatum()
        //页面滚动控件
        binding.apply {
            pager.offscreenPageLimit = indicatorTitles.size
            pager.adapter = MainAdaptor(this@MainActivity, indicatorTitles.size)
        }

        //底部tab
        for (i in indicatorTitles.indices) {
            ItemTabBinding.inflate(layoutInflater).apply {
                content.setText(indicatorTitles[i])
                icon.setImageResource(indicatorIcons[i])
                binding.indicator.addView(root)
            }
        }
        ViewPager2Delegate.install(binding.pager, binding.indicator, false)
        val action = intent.action
        if (Constant.ACTION_LOGIN == action) {
            startActivity(LoginHomeActivity::class.java)
        }
    }

    companion object {
        /**
         * 底部指示器（tab）文本，图标，选中的图标
         */

        private val indicatorTitles =
            intArrayOf(
                R.string.discovery,
                R.string.video,
                R.string.category,
                R.string.me
            )

        private val indicatorIcons = intArrayOf(
            R.drawable.selector_tab_discovery,
            R.drawable.selector_tab_video,
            R.drawable.selector_tab_category,
            R.drawable.selector_tab_me
        )
    }
}