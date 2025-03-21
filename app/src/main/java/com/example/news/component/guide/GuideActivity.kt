package com.example.news.component.guide

import android.content.Intent
import com.example.news.component.main.MainActivity
import com.example.news.R
import com.example.news.activity.BaseViewModelActivity
import com.example.news.databinding.ActivityGuideBinding
import com.example.news.util.Constant
import com.example.news.util.PreferenceUtil

/**
 * 左右滚动的引导界面
 * 如果想实现更复杂的效果，例如：滚动时文本缩放等效果，可以使用类似这样的框架：
 * https://github.com/bingoogolapple/BGABanner-Android
 */
class GuideActivity : BaseViewModelActivity<ActivityGuideBinding>() {
    private lateinit var adapter: GuideAdapter

    override fun initListeners() {
        super.initListeners()
        binding.loginOrRegister.setOnClickListener{
            setShowGuide()
            val intent = Intent(this, MainActivity::class.java)
            intent.action = Constant.ACTION_LOGIN
            startActivity(intent)
            finish()
        }

        binding.experienceNow.setOnClickListener{
            setShowGuide()
            startActivityAfterFinishThis(MainActivity::class.java)
        }
    }

    override fun initDatum() {
        super.initDatum()
        adapter = GuideAdapter(this, supportFragmentManager)
        //设置Adapter到轮播控件
        binding.list.adapter = adapter
        //让指示器根据轮播控件工作
        binding.indicator.setViewPager(binding.list)

        //Adapter注册数据源观察者
        adapter.registerDataSetObserver(binding.indicator.dataSetObserver)
        //准备引导界面图片
        val datum: MutableList<Int> = ArrayList()
        datum.add(R.drawable.guide1)
        datum.add(R.drawable.guide2)
        datum.add(R.drawable.guide3)
        datum.add(R.drawable.guide4)
        datum.add(R.drawable.guide5)

        //设置图片数据到Adapter
        adapter.setDatum(datum)
    }
    private fun setShowGuide() {
        PreferenceUtil.setShowGuide(false)
    }
}