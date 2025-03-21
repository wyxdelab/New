package com.example.news.component.content

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.FragmentContentBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.Constant
import kotlinx.coroutines.launch

/**
 * 内容界面
 */
class ContentFragment: BaseViewModelFragment<FragmentContentBinding>() {

    private lateinit var contentAdaptor: ContentAdaptor
    private lateinit var viewModel: ContentViewModel

    override fun initViews() {
        super.initViews()
        binding.list.apply {
            layoutManager = LinearLayoutManager(hostActivity)
            val decoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            addItemDecoration(decoration)
        }
    }

    override fun initDatum() {
        super.initDatum()
        viewModel = ViewModelProvider(this).get(ContentViewModel::class.java)
        contentAdaptor = ContentAdaptor()
        binding.list.adapter = contentAdaptor
        lifecycleScope.launch {
            viewModel.data.collect{
                Log.d(TAG, "initDatum: ${it.data!![0].title}")
                contentAdaptor.submitList(it.data)
            }
        }
        viewModel.loadMore()


    }
    companion object {
        const val TAG = "ContentFragment"
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