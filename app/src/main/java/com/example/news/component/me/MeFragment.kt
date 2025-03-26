package com.example.news.component.me

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.drake.channel.receiveEvent
import com.example.news.R
import com.example.news.component.address.AddressActivity
import com.example.news.component.cart.CartActivity
import com.example.news.component.login.LoginHomeActivity
import com.example.news.component.login.LoginStatusChangedEvent
import com.example.news.component.order.OrderActivity
import com.example.news.component.product.ProductActivity
import com.example.news.component.profile.UserInfoChanged
import com.example.news.component.setting.SettingActivity
import com.example.news.component.user.User
import com.example.news.component.userdetail.UserDetailActivity
import com.example.news.databinding.FragmentMeBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.util.ImageUtil
import com.example.news.util.PreferenceUtil
import hide
import kotlinx.coroutines.launch
import show

class MeFragment: BaseViewModelFragment<FragmentMeBinding>() {
    private lateinit var viewModel: MeViewModel

    override fun initDatum() {
        super.initDatum()
        viewModel = ViewModelProvider(this).get(MeViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data.collect {
                data -> showData(data)
            }
        }

        viewModel.loadUserData()
    }
    private fun showData(data: User) {
        //显示昵称
        binding.nickname.text = data.nickname

        binding.number.text = resources.getString(R.string.message_no_format, data.id)

        //显示头像
        ImageUtil.showAvatar(binding.icon, data.icon)
    }

    private fun showUserInfo() {
        if (PreferenceUtil.isLogin()) {
            //已经登录了

            //获取用户信息
            viewModel.loadUserData()
            binding.number.show()
            binding.code.show()
        } else {
            showNotLogin()
        }
    }

    /**
     * 显示未登录状态
     */
    private fun showNotLogin() {
        binding.nickname.setText(R.string.login_or_register)
        binding.icon.setImageResource(R.drawable.default_avatar)
        binding.number.hide()
        binding.code.hide()
    }

    override fun initListeners() {
        super.initListeners()

        //用户信息按钮
        binding.profile.setOnClickListener {
            if (PreferenceUtil.isLogin()) {
                startActivityExtraId(UserDetailActivity::class.java, PreferenceUtil.getUserId())
            } else {
                startActivity(LoginHomeActivity::class.java)
            }
        }

        binding.mall.setOnClickListener { v ->
            startActivity(ProductActivity::class.java)
        }
        binding.order.setOnClickListener { v ->
            loginAfter {
                startActivity(OrderActivity::class.java)
            }
        }
        binding.cart.setOnClickListener { v -> loginAfter { startActivity(CartActivity::class.java) } }

        binding.address.setOnClickListener { v ->
            loginAfter {
                startActivity(AddressActivity::class.java)
            }
        }

        //设置按钮
        binding.setting.setOnClickListener {
            startActivity(SettingActivity::class.java)
        }

        //当前用户信息改变了事件
        receiveEvent<UserInfoChanged> {
            viewModel.loadUserData()
        }

        receiveEvent<LoginStatusChangedEvent> {
            showUserInfo()
        }

        binding.code.setOnClickListener {
//            startActivityExtraId(CodeActivity::class.java,PreferenceUtil.getUserId())
        }
    }
    companion object {
        fun newInstance(): MeFragment {
            val args = Bundle()

            val fragment = MeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}