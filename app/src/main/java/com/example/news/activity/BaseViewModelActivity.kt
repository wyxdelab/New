package com.example.news.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.news.R
import com.example.news.util.ReflectUtil

/**
 * 通用ViewModel Activity
 * 包括ViewBinding，主要是处理每次要setContentView
 * 以及自动创建ViewModel
 * 以及viewModel的通用观察处理
 */
open class BaseViewModelActivity<VB : ViewBinding> : BaseLogicActivity() {
    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        //调用inflate方法，创建viewBinding
        binding = ReflectUtil.newViewBinding(layoutInflater, javaClass)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

    }
}