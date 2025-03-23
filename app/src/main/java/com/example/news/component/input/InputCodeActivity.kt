package com.example.news.component.input

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.news.component.login.BaseLoginActivity
import com.example.news.databinding.ActivityInputCodeBinding
import com.example.news.util.Constant
import com.king.view.splitedittext.SplitEditText
import kotlinx.coroutines.launch

/**
 * 输入验证码界面
 *
 *
 * 可以是手机验证，也可以邮箱验证码
 */
class InputCodeActivity : BaseLoginActivity<ActivityInputCodeBinding>() {

    private lateinit var viewModel: InputCodeViewModel

    override fun initDatum() {
        super.initDatum()
        val viewModelFactory =
            InputCodeViewModelFactory(intent.getParcelableExtra(Constant.DATA)!!)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(InputCodeViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.codeLogin
                .collect { data ->
                    loginViewModel.login(data)
                }
        }

        viewModel.codeSendTarget.observe(this) {
            binding.codeSendTarget.text = it
        }

        viewModel.sendText.observe(this) {
            binding.send.text = it
        }

        viewModel.sendEnable.observe(this) {
            binding.send.isEnabled = it
        }

        viewModel.loadData()
    }

    override fun initListeners() {
        super.initListeners()
        //设置监听
        binding!!.code.setOnTextInputListener(object : SplitEditText.OnSimpleTextInputListener() {
            override fun onTextInputCompleted(s: String) {
                viewModel.processNext(s)
            }
        })
        binding!!.send.setOnClickListener { viewModel.sendCode() }
    }

    companion object {
        fun start(context: Context, data: InputCodePageData) {
            val intent = Intent(context, InputCodeActivity::class.java)
            intent.putExtra(Constant.DATA, data)
            context.startActivity(intent)
        }
    }

}