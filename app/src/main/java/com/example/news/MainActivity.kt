package com.example.news

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.activity.BaseViewModelActivity
import com.example.news.component.login.LoginHomeActivity
import com.example.news.databinding.ActivityMainBinding
import com.example.news.util.Constant

class MainActivity : BaseViewModelActivity<ActivityMainBinding>() {
    override fun initDatum() {
        super.initDatum()
        val action = intent.action
        if (Constant.ACTION_LOGIN == action) {
            startActivity(LoginHomeActivity::class.java)
        }
    }
}