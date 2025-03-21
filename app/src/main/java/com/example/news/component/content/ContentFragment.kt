package com.example.news.component.content

import android.os.Bundle
import com.example.news.databinding.FragmentContentBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.util.Constant

/**
 * 内容界面
 */
class ContentFragment: BaseViewModelFragment<FragmentContentBinding>() {
    companion object {
        fun newInstance(categoryId: String? = null): ContentFragment{
            val args = Bundle()
            categoryId?.let {
                args.putString(Constant.ID,it)
            }
            val fragment = ContentFragment()
            fragment.arguments = args
            return fragment
        }
    }

}