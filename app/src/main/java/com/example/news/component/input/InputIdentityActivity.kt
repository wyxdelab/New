package com.example.news.component.input

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.drake.channel.receiveEvent
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.login.LoginStatusChangedEvent
import com.example.news.databinding.ActivityInputIdentityBinding
import com.example.news.util.Constant
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

/**
 * 输入手机号和邮箱的验证码登录通用界面
 */
class InputIdentityActivity : BaseTitleActivity<ActivityInputIdentityBinding>() {


    private lateinit var viewModel: InputIdentityViewModel

    override fun initListeners() {
        super.initListeners()
        binding.username.doAfterTextChanged {
            val notBlank = StringUtils.isNotBlank(it.toString().trim())
            binding.primary.isEnabled = notBlank
        }

        binding.primary.setOnClickListener(View.OnClickListener {
            viewModel.primaryClick(
                binding.username.text.toString().trim()
            )
        })


    }
    override fun initDatum() {
        super.initDatum()

        val viewModelFactory =
            InputIdentityViewModelFactory(intent.getIntExtra(Constant.STYLE, -1))
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(InputIdentityViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.title
                .collect { data ->
                    setTitle(data)
                }
        }

        lifecycleScope.launch {
            viewModel.toNext
                .collect { data ->
                    InputCodeActivity.start(hostActivity, data)
                }
        }
        viewModel.loadData()

        receiveEvent<LoginStatusChangedEvent> {
            finish()
        }
    }

    companion object {
        /**
         * 启动界面
         *
         * @param context
         */
        fun start(context: Context, style: Int = Constant.STYLE_CODE_LOGIN) {
            val intent = Intent(context, InputIdentityActivity::class.java)
            intent.putExtra(Constant.STYLE, style)
            context.startActivity(intent)
        }
    }
}