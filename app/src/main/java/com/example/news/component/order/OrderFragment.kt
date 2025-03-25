package com.example.news.component.order

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.news.component.orderdetail.OrderDetailActivity
import com.example.news.databinding.RecyclerViewBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.util.Constant
import kotlinx.coroutines.launch

/**
 * 订单界面
 */
class OrderFragment : BaseViewModelFragment<RecyclerViewBinding>() {
    private lateinit var viewModel: OrderViewModel
    private lateinit var adapter: OrderAdapter
    override fun initDatum() {
        super.initDatum()
        adapter = OrderAdapter()
        binding.list.adapter = adapter

        val viewModelFactory = OrderViewModelFactory(extraInt(Constant.STYLE))
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrderViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    adapter!!.submitList(data)
                }
        }

        lifecycle.addObserver(viewModel)
    }

    override fun initListeners() {
        super.initListeners()
        adapter!!.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, view: View?, position: Int ->
            val data = adapter.getItem(position) as Order
            startActivityExtraId(OrderDetailActivity::class.java, data.id!!)
        }
    }

    companion object {
        fun newInstance(status: Int): OrderFragment {
            val args = Bundle()
            args.putInt(Constant.STYLE, status)
            val fragment = OrderFragment()
            fragment.arguments = args
            return fragment
        }
    }
}