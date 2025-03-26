package com.example.news.component.category

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.news.databinding.FragmentCategoryBinding
import com.example.news.fragment.BaseViewModelFragment
import kotlinx.coroutines.launch

class CategoryFragment: BaseViewModelFragment<FragmentCategoryBinding>() {
    private lateinit var viewModel: CategoryViewModel
    private val leftCategoryAdapter = LeftCategoryAdapter()
    private val rightCategoryAdapter = RightCategoryAdapter()

    override fun initDatum() {
        super.initDatum()
        binding.left.adapter = leftCategoryAdapter
        binding.right.adapter = rightCategoryAdapter

        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.leftData
                .collect { data ->
                    leftCategoryAdapter.submitList(data)
                    viewModel.loadRightData(data[0].id!!)
                }
        }

        lifecycleScope.launch {
            viewModel.rightData
                .collect { data ->
                    rightCategoryAdapter.submitList(data)
                }
        }

        viewModel.loadLeftData()
    }

    override fun initListeners() {
        super.initListeners()
        leftCategoryAdapter.setOnItemClickListener { adapter, view, position ->
            leftCategoryAdapter.setSelectedIndex(position)
            val r = adapter.getItem(position) as Category
            viewModel.loadRightData(r.id!!)
        }
    }


    companion object {
        fun newInstance(): CategoryFragment {
            val args = Bundle()

            val fragment = CategoryFragment()
            fragment.arguments = args
            return fragment
        }
    }

}