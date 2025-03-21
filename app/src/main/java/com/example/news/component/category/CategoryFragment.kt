package com.example.news.component.category

import android.os.Bundle
import com.example.news.databinding.FragmentCategoryBinding
import com.example.news.databinding.FragmentHomeBinding
import com.example.news.fragment.BaseViewModelFragment

class CategoryFragment: BaseViewModelFragment<FragmentCategoryBinding>() {

    companion object {
        fun newInstance(): CategoryFragment {
            val args = Bundle()

            val fragment = CategoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

}