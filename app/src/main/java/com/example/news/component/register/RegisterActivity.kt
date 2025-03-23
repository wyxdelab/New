package com.example.news.component.register

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.login.BaseLoginActivity
import com.example.news.databinding.ActivityRegisterBinding

class RegisterActivity : BaseLoginActivity<ActivityRegisterBinding>() {
    private lateinit var viewModel: RegisterViewModel

    override fun initDatum() {
        super.initDatum()
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        initViewModel(viewModel)

        //观察成功结果
        viewModel.success.observe(this) {
            //成功了

            //调用父类自动登录方法
            loginViewModel.login(binding.phone.text.toString(), binding.password.text.toString())
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.primary.setOnClickListener {
            viewModel.register(
                binding.nickname.text.toString().trim(),
                binding.phone.text.toString().trim(),
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim(),
                binding.confirmPassword.text.toString().trim()
            )
        }


    }
}