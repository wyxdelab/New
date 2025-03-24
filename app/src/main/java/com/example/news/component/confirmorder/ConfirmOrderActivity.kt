package com.example.news.component.confirmorder

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.drake.channel.receiveEvent
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.address.AddressActivity
import com.example.news.component.address.AddressChangedEvent
import com.example.news.component.product.Product
import com.example.news.databinding.ActivityConfirmOrderBinding
import com.example.news.databinding.ItemOrderProductBinding
import com.example.news.util.Constant
import com.example.news.util.ImageUtil
import hide
import kotlinx.coroutines.launch
import show

class ConfirmOrderActivity : BaseTitleActivity<ActivityConfirmOrderBinding>() {
    private lateinit var viewModel: ConfirmOrderViewModel

    override fun initDatum() {
        super.initDatum()

        val viewModelFactory = ConfirmOrderViewModelFactory(extraId(), getIntent().getStringArrayListExtra(Constant.DATA))

        viewModel = ViewModelProvider(this, viewModelFactory).get(ConfirmOrderViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    showData(data)
                }
        }

        viewModel.loadData()
    }

    private fun showData(data: ConfirmOrderResponse) {
        //地址
        if (data.address == null) {
            binding.address.topAddressContainer.hide()
            binding.address.detail.setText(R.string.select_address)
        } else {
            binding.address.topAddressContainer.show()
            binding.address.defaultAddress.show(data.address!!.isDefault())
            binding.address.contact.setText(data.address!!.getContact())
            binding.address.area.setText(data.address!!.getReceiverArea())
            binding.address.detail.setText(data.address!!.detail)

        }

        //显示商品
        binding.productContainer.removeAllViews()

        var product: Product
        var itemBinding: ItemOrderProductBinding
        for (it in data.carts) {
            itemBinding = ItemOrderProductBinding.inflate(
                getLayoutInflater(),
                this.binding.productContainer,
                false
            )
            product = it.product

            //图标
            ImageUtil.show(itemBinding.icon, product.icons[0])

            //标题
            itemBinding.title.setText(product.title)

            //价格
            val price: String = getResources().getString(R.string.price, product.priceFloat)
            itemBinding.price.setText(price)

            //数量
            itemBinding.count.setText(
                getResources().getString(
                    R.string.product_count,
                    it.count
                )
            )
            binding.productContainer.addView(itemBinding.getRoot())
        }

        //商品总价
        binding.totalPrice.setText(getResources().getString(R.string.price, data.totalPriceFloat))
        binding.total.setText(
            getResources().getString(
                R.string.total_count,
                data.carts.size
            )
        )

        //还需支付
        binding.price.setText(getResources().getString(R.string.price, data.priceFloat))
    }

    override fun initListeners() {
        super.initListeners()
        binding.address.root.setOnClickListener {
            AddressActivity.startWithSelect(hostActivity)
        }

        receiveEvent<AddressChangedEvent> {
            viewModel.setAddressId(it.data.id!!)
        }
    }
}