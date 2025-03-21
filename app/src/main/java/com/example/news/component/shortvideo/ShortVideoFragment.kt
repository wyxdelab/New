package com.example.news.component.shortvideo

import android.os.Bundle
import com.example.news.databinding.FragmentHomeBinding
import com.example.news.databinding.FragmentShortVideoBinding
import com.example.news.fragment.BaseViewModelFragment

class ShortVideoFragment: BaseViewModelFragment<FragmentShortVideoBinding>() {

    companion object {
        fun newInstance(): ShortVideoFragment {
            val args = Bundle()

            val fragment = ShortVideoFragment()
            fragment.arguments = args
            return fragment
        }
    }

}