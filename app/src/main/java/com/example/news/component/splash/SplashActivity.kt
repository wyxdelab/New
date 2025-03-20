package com.example.news.component.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.activity.BaseLogicActivity
import com.example.news.activity.BaseViewModelActivity
import com.example.news.component.guide.GuideActivity
import com.example.news.databinding.ActivitySplashBinding
import com.example.news.util.DefaultPreferenceUtil
import com.example.news.util.PreferenceUtil
import com.example.superui.util.SuperDateUtil
import com.permissionx.guolindev.PermissionX

class SplashActivity : BaseViewModelActivity<ActivitySplashBinding>() {


    override fun initDatum() {
        super.initDatum()
        if (DefaultPreferenceUtil.getInstance(this).isAcceptTermsServiceAgreement) {
            //已经同意了用户协议
            requestPermssion()
        } else {
            showTermsServiceAgreementDialog()
        }

    }

    private fun requestPermssion() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    binding.root.postDelayed({
                        prepareNext()
                    }, 1000)

                } else {
                    finish()
                }
            }
    }

    private fun prepareNext() {
        Log.d(TAG, "prepare")
        if (PreferenceUtil.isShowGuide()) {//需要引导界面
            startActivityAfterFinishThis(GuideActivity::class.java)
            return
        }
        //跳转到下一个界面

    }

    private fun showTermsServiceAgreementDialog() {
        TermServiceDialogFragment.show(supportFragmentManager
        ) { Log.d(TAG, "primary onClick")
            DefaultPreferenceUtil.getInstance(this).setAcceptTermsServiceAgreement()
            requestPermssion()
        }

    }

    companion object {
        const val TAG = "SplashActivity"
    }
}