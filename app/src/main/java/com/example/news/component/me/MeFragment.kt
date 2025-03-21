package com.example.news.component.me

import android.os.Bundle
import com.example.news.databinding.FragmentHomeBinding
import com.example.news.databinding.FragmentMeBinding
import com.example.news.fragment.BaseViewModelFragment

class MeFragment: BaseViewModelFragment<FragmentMeBinding>() {

    companion object {
        fun newInstance(): MeFragment {
            val args = Bundle()

            val fragment = MeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}