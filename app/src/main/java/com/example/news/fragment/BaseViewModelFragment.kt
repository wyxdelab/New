package com.example.news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.news.util.ReflectUtil

open class BaseViewModelFragment<VB : ViewBinding> : BaseCommonFragment(){
    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    override fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //调用inflate方法，创建viewBinding
        _binding = ReflectUtil.newViewBinding(layoutInflater, javaClass)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}