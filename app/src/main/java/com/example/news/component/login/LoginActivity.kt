package com.example.news.component.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.news.AppContext
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.articleldetail.ArticleDetailViewModel
import com.example.news.config.Config
import com.example.news.databinding.ActivityLoginBinding

/**
 * 登录界面
 */
class LoginActivity : BaseTitleActivity<ActivityLoginBinding>() {
    private lateinit var viewModel: LoginViewModel

    override fun initDatum() {
        super.initDatum()
        if (Config.DEBUG) {
            binding.username.setText("13141111222")
            binding.password.setText("ixueaedu")
        }
        viewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        initViewModel(viewModel)

        viewModel.success.observe(this) {
            AppContext.instance.onLogin()
            finish()
        }

    }

    override fun initListeners() {
        super.initListeners()
        binding.primary.setOnClickListener {
            viewModel.login(
                binding.username.text.toString().trim(),
                binding.password.text.toString().trim()
            )
        }
    }
}