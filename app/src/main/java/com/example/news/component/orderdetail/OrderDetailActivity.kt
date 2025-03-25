package com.example.news.component.orderdetail

import android.content.Context
import android.content.Intent
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
import com.example.news.component.order.Order
import com.example.news.component.pay.AlipayStatusChangedEvent
import com.example.news.component.pay.PayActivity
import com.example.news.component.product.Product
import com.example.news.databinding.ActivityOrderDetailBinding
import com.example.news.databinding.ItemOrderProductBinding
import com.example.news.util.Constant
import com.example.news.util.ImageUtil
import com.example.superui.util.SuperDateUtil
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import show

class OrderDetailActivity : BaseTitleActivity<ActivityOrderDetailBinding>() {
    private lateinit var viewModel: OrderDetailViewModel

    override fun initViews() {
        super.initViews()
        //状态栏文字白色
        QMUIStatusBarHelper.setStatusBarDarkMode(this)
        val color: Int = getColor(R.color.primary)
        setStatusBarColor(color)
        binding.bar.root.setBackgroundColor(color)
    }

    override fun initDatum() {
        super.initDatum()
        val viewModelFactory = OrderDetailViewModelFactory(extraId(), extraStyle())
        viewModel = ViewModelProvider(this, viewModelFactory).get(OrderDetailViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    showData(data)
                }
        }

        lifecycle.addObserver(viewModel)

    }

    private fun showData(data: Order) {
        //订单状态
        binding.status.setText(data.getStatusFormat())

        //地址
        binding.address.topAddressContainer.show()
        binding.address.defaultAddress.show(data.address!!.isDefault())
        binding.address.contact.setText(data.address!!.getContact())
        binding.address.area.setText(data.address!!.getReceiverArea())
        binding.address.detail.setText(data.address!!.detail)

        //显示商品
        binding.productContainer.removeAllViews()
        var itemBinding: ItemOrderProductBinding
        var product: Product
        for (it in data.products!!) {
            itemBinding = ItemOrderProductBinding.inflate(
                layoutInflater,
                binding.productContainer,
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

        //还需支付
        binding.amountPay.setText(getResources().getString(R.string.price, data.priceFloat))
        binding.price.text = binding.amountPay.text

        //下单时间
        binding.orderTime.setText(SuperDateUtil.yyyyMMddHHmmss(data.createdAt!!))

        //订单号
        binding.orderNo.setText(data.number)

        //订单来源
        binding.orderSource.setText(data.getSourceFormat())

        //支付平台
        binding.payPlatform.setText(data.getOriginFormat())

        //支付渠道
        binding.payChannel.setText(data.getChannelFormat())
        if (data.isWaitPay) {
            //待付款状态
            binding.payControlContainer.show()
            binding.payRemainingTime.show()

            //最后支付时间
            //订单创建时间后，15分钟内完成支付
            var dateTime: DateTime = DateTime(data.createdAt)

            //加15分钟
            dateTime = dateTime.plusMinutes(15)
            binding.payRemainingTime.setText(
                getString(R.string.payment_before, dateTime.toString(SuperDateUtil.HHmmss))
            )
        } else if (data.isShipped) {
            //待发货
            binding.payWaitShipContainer.show()

            //显示退款菜单，这里就不再实现了，因为后面还有很多逻辑
            //查看物流，评价等，但基本上没什么难点了，就是逻辑了
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.primary.setOnClickListener { v ->
            PayActivity.startFromOrder(
                hostActivity,
                viewModel.id
            )
        }

    }


}