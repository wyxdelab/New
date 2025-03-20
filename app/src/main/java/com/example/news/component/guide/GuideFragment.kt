package com.example.news.component.guide

import android.os.Bundle
import com.example.news.databinding.FragmentGuideBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.util.Constant

class GuideFragment: BaseViewModelFragment<FragmentGuideBinding>() {
    override fun initDatum() {
        super.initDatum()
        var data = requireArguments().getInt(Constant.ID)
        binding.icon.setImageResource(data)
    }

    companion object {
        fun newInstance(data: Int): GuideFragment{
            val args = Bundle()
            args.putInt(Constant.ID, data)
            val fragment = GuideFragment()
            fragment.arguments = args
            return fragment
        }
    }

}