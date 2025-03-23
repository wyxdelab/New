package com.example.news.component.login

import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.drake.channel.receiveEvent
import com.example.news.AppContext
import com.example.news.activity.BaseTitleActivity

/**
 * 登录通用界面
 */
open class BaseLoginActivity<VB : ViewBinding> : BaseTitleActivity<VB>() {
    lateinit var loginViewModel: LoginViewModel

    override fun initDatum() {
        super.initDatum()
        loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        initViewModel(loginViewModel)

        //观察成功结果
        loginViewModel.success.observe(this) {
            //成功了

            //执行登录后操作
            AppContext.instance.onLogin()
//            AppContext.instance.loginChat()
        }

        receiveEvent<LoginStatusChangedEvent> {
            finish()
        }
    }

}