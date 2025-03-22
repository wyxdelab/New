package com.example.news.component.login

import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.activity.BaseViewModelActivity
import com.example.news.databinding.ActivityLoginHomeBinding
import com.example.superui.util.SuperTextUtil

class LoginHomeActivity : BaseViewModelActivity<ActivityLoginHomeBinding>() {
    override fun initDatum() {
        super.initDatum()
        SuperTextUtil.setLinkColor(binding.userAgreement, ContextCompat.getColor(hostActivity, R.color.link))

        val content = Html.fromHtml(getString(R.string.user_agreement))
        binding.userAgreement.text = content
    }
}