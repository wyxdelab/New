package com.example.news.component.pay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.order.Order
import com.example.news.component.orderdetail.OrderDetailActivity
import com.example.news.databinding.ActivityPayBinding
import com.example.news.util.Constant
import com.example.news.util.PayUtil
import com.example.superui.dialog.SuperDialog
import com.example.superui.extension.shortToast
import com.example.superui.util.SuperDateUtil
import com.ixuea.courses.mymusic.component.pay.PayViewModelFactory
import kotlinx.coroutines.launch
import me.shihao.library.XRadioGroup

class PayActivity : BaseTitleActivity<ActivityPayBinding>(), XRadioGroup.OnCheckedChangeListener {
    private var isShipped: Boolean = false
    private lateinit var viewModel: PayViewModel
    private var countDownTimer: CountDownTimer? = null

    override fun initViews() {
        super.initViews()

        //默认选中支付宝
        binding.radioGroupPay.check(R.id.radio_alipay)
    }

    override fun initDatum() {
        super.initDatum()
        val viewModelFactory = PayViewModelFactory(extraInt(Constant.STYLE), extraId())
        viewModel = ViewModelProvider(this, viewModelFactory).get(PayViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    showData(data)
                }
        }

//        lifecycleScope.launch {
//            viewModel.processAlipay
//                .collect { data ->
//                    PayUtil.alipay(hostActivity, data)
//                }
//        }

        viewModel.loadData()
    }

    override fun initListeners() {
        super.initListeners()
        //设置支付方式改变监听器
        binding.radioGroupPay.setOnCheckedChangeListener(this)

        //支付按钮点击
//        binding.primary.setOnClickListener { viewModel.loadPayData() }
        binding.primary.setOnClickListener {
            R.string.pay_success.shortToast()

            startActivityExtraIdAndStyle(OrderDetailActivity::class.java, viewModel.id, Constant.STYLE_SHIPPED)

            //发布状态

            finish()
        }

        //支付宝支付状态改变了
//        receiveEvent<AlipayStatusChangedEvent> {
//            prepareAlipayStatusChanged(it)
//        }

//
//        //微信支付状态改变了
//        receiveEvent<WechatPayStatusChangedEvent> {
//            processWechatPayStatusChanged(it)
//        }
    }
//
//    private fun processWechatPayStatusChanged(event: WechatPayStatusChangedEvent) {
//        if (BaseResp.ErrCode.ERR_OK === event.data.errCode) {
//            //本地支付成功
//
//            //不能依赖本地支付结果
//            //一定要以服务端为准
//            showLoading(R.string.hint_pay_wait)
//
//            //延时3秒
//            //因为回调我们服务端可能有延迟
//            binding.primary.postDelayed({ checkPayStatus() }, 3000)
//
//            //这里就不根据服务端判断了
//            //购买成功统计
//            AnalysisUtil.onPurchase(hostActivity, true, viewModel.order)
//        } else if (BaseResp.ErrCode.ERR_USER_CANCEL === event.data.errCode) {
//            //支付取消
//            R.string.error_pay_cancel.shortToast()
//
//            AnalysisUtil.onPurchase(hostActivity, false, viewModel.order)
//        } else {
//            //支付失败
//            R.string.error_pay_failed.shortToast()
//
//            AnalysisUtil.onPurchase(hostActivity, false, viewModel.order)
//        }
//    }

//    private fun prepareAlipayStatusChanged(data: AlipayStatusChangedEvent) {
//        val resultStatus = data.data.resultStatus!!
//        if ("9000" == resultStatus) {
//            //本地支付成功
//
//            //不能依赖本地支付结果
//            //一定要以服务端为准
//            showLoading(R.string.hint_pay_wait)
//
//            //延时3秒
//            //因为支付宝回调我们服务端可能有延迟
//            binding.primary.postDelayed({ checkPayStatus() }, 3000)
//
//            //这里就不根据服务端判断了
//            //购买成功统计
////            AnalysisUtil.onPurchase(hostActivity, true, viewModel.order)
//        } else if ("6001" == resultStatus) {
//            //支付取消
//            R.string.error_pay_cancel.shortToast()
//
////            AnalysisUtil.onPurchase(hostActivity, false, viewModel.order)
//        } else {
//            //支付失败
//            R.string.error_pay_failed.shortToast()
//
////            AnalysisUtil.onPurchase(hostActivity, false, viewModel.order)
//        }
//    }

    /**
     * 检查支付状态
     */
//    private fun checkPayStatus() {
//        //隐藏加载对话框
//        hideLoading()
//        isShipped = true
//        //请求订单详情
//        viewModel.loadData()
//    }

    private fun showData(data: Order) {
        binding.price.text = resources.getString(R.string.price, data.priceFloat)

//        if (data.isShipped) {
            //已经支付了
        if (isShipped) {
            next()
        } /*else {
            startCountDown(data)
        }*/
    }

    override fun onBackPressed() {
        showBackConfirmDialog()
    }

    /**
     * 显示返回确认对话框
     */
    private fun showBackConfirmDialog() {
        SuperDialog.newInstance(supportFragmentManager)
            .setTitleRes(R.string.confirm_exit_pay)
            .setMessageRes(R.string.confirm_exit_pay_message) //确认按钮
            .setOnClickListener(null, R.string.continue_pay) //取消按钮
            .setOnCancelClickListener({
                next()
            }, R.string.confirm_departure)
            .show()
    }

    private fun next() {
        finish()
        if (viewModel.style == Constant.STYLE_CONFIRM_ORDER) {
            //只有从确认订单跳转过来，才需要跳转到订单详情
            startActivityExtraId(OrderDetailActivity::class.java, viewModel.id)
        }
    }

    companion object {
        /**
         * 启动该界面
         *
         * @param context
         * @param style   类型
         * @param data    数据
         */
        fun start(context: Context, style: Int, data: String) {
            val intent = Intent(context, PayActivity::class.java)
            intent.putExtra(Constant.STYLE, style)
            intent.putExtra(Constant.ID, data)
            context.startActivity(intent)
        }

        /**
         * 从确认订单跳转过来
         *
         * @param data
         */
        fun startFromConfirmOrder(context: Context, data: String) {
            start(context!!, Constant.STYLE_CONFIRM_ORDER, data)
        }

        /**
         * 从订单（订单详情，订单列表等）跳转过来
         *
         * @param data
         */
        fun startFromOrder(context: Context, data: String) {
            start(context!!, Constant.STYLE_ORDER, data)
        }
    }

    /**
     * 支付方式单选按钮改变了
     *
     * @param group
     * @param checkedId
     */
    override fun onCheckedChanged(group: XRadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.radio_alipay -> viewModel.setChannel(Constant.ALIPAY)
            R.id.radio_wechat -> viewModel.setChannel(Constant.WECHAT)
            R.id.radio_huawei_stage -> viewModel.setChannel(Constant.HUABEI_STAGE)
        }
    }

//    private fun startCountDown(data: Order) {
//        if (countDownTimer != null) {
//            return
//        }
//
//        //15分钟倒计时
//        //注意：这里倒计时完成后，由于目前服务端实现不是很精确，所以订单可能此时还没有关闭
//        //真实项目中服务端用队列实现，可以提交精确时间
//        //如果还觉得的保险，客户端/获取服务端在返回数据时可以在判断时间，如果当前时间大于订单超时时间
//        //也显示已经关闭，或者服务端在关闭，这样一系列方法实现后，就可以做到相对更精确关闭超时订单了
//        //但这样实现太复杂了，毕竟这个课程不是专门讲解商城的，详细的在商城项目讲解
//        //订单创建时间后，15分钟内完成支付
//        var createdAt: DateTime = DateUtil.parse(data.createdAt, UTC_MS_WITH_XXX_OFFSET_PATTERN)
//
//        //创建时间增加15分钟
//        createdAt = DateUtil.offsetMinute(createdAt, 15)
//        val now: DateTime = DateTime.now()
//
//        //计算时间差；小时间，大时间
//        val between: Long = DateUtil.between(now, createdAt, DateUnit.MS)
//        if (between <= 1000) {
//            //小于1秒，已经超时了，可以弹窗提示，这里就直接关闭
//            finish()
//        }
//        countDownTimer = object : CountDownTimer(between, 1000) {
//            /**
//             * 每次间隔调用
//             *
//             *
//             * 显示时:分:秒
//             *
//             * @param millisUntilFinished
//             */
//            override fun onTick(millisUntilFinished: Long) {
//                val result: String = SuperDateUtil.ms2hms(millisUntilFinished.toInt())
//                binding.payRemainingTime.text = getString(R.string.pay_remaining_time, result)
//            }
//
//            /**
//             * 倒计时完成
//             */
//            override fun onFinish() {
//                //可以弹窗提示，支付超时，已经自动关闭订单
//                //如果还需要购买，请重新下单
//                //这里就直接关闭界面了
//                finish()
//            }
//        }
//
//        //启动定时器
//        countDownTimer!!.start()
//    }
}