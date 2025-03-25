package com.example.news.component.order

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.databinding.ActivityOrderBinding
import com.example.news.util.Constant
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class OrderActivity : BaseTitleActivity<ActivityOrderBinding>() {
    private lateinit var adapter: OrderPageAdapter

    override fun initViews() {
        super.initViews()
        binding.list.offscreenPageLimit = 4

    }

    override fun initDatum() {
        super.initDatum()
        adapter = OrderPageAdapter(hostActivity)
        binding.list.setAdapter(adapter)
        adapter.setDatum(
            listOf(
                Constant.VALUE_NO,
                Constant.WAIT_PAY,
                Constant.WAIT_RECEIVED,
                Constant.WAIT_COMMENT
            )
        )

        //创建通用指示器
        val commonNavigator = CommonNavigator(hostActivity)

        //设置适配器
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            /**
             * 指示器数量
             *
             * @return
             */
            override fun getCount(): Int {
                return indicatorTitles.size
            }

            /**
             * 返回当前位置的标题
             *
             * @param context
             * @param index
             * @return
             */
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                //创建简单的文本控件
                val titleView = SimplePagerTitleView(context)

                //默认颜色
                titleView.setNormalColor(getColor(R.color.black80))

                //选中后的颜色
                titleView.setSelectedColor(getColor(R.color.text_price))

                //设置显示的文本
                titleView.setText(indicatorTitles[index])

                //点击回调监听
                titleView.setOnClickListener(View.OnClickListener { //让ViewPager跳转到指定位置
                    binding.list.currentItem = index
                })
                return titleView
            }

            /**
             * 返回指示器
             * 就是下面那条线
             *
             * @param context
             * @return
             */
            override fun getIndicator(context: Context): IPagerIndicator {
                //创建一条线
                val indicator = LinePagerIndicator(context)

                //线的宽度和内容一样
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT)

                //高亮颜色
                indicator.setColors(getColor(R.color.text_price))
                return indicator

                //返回null表示不显示指示器
                //return null;
            }
        }
        //如何位置显示不下指示器
        //是否自动调整
        commonNavigator.setAdjustMode(true)

        //设置导航器
        binding.indicator.setNavigator(commonNavigator)
        binding.list.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.indicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.indicator.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                binding.indicator.onPageScrollStateChanged(state)
            }
        })
    }

    companion object {
        private val indicatorTitles = intArrayOf(
            R.string.whole,
            R.string.wait_pay,
            R.string.wait_received,
            R.string.wait_comment
        )
    }
}