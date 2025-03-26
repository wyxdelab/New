package com.example.news.component.search

import android.os.Bundle
import com.example.news.databinding.ListBinding
import com.example.news.fragment.BaseViewModelFragment

class OtherFragment : BaseViewModelFragment<ListBinding>() {

    companion object {
        fun newInstance(): OtherFragment {
            val args = Bundle()

            val fragment = OtherFragment()
            fragment.arguments = args
            return fragment
        }
    }
}