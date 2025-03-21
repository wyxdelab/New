package com.example.news.component.home

import android.os.Bundle
import com.example.news.databinding.FragmentHomeBinding
import com.example.news.fragment.BaseViewModelFragment

class HomeFragment: BaseViewModelFragment<FragmentHomeBinding>() {

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}