package com.example.news.component.splash

import android.Manifest
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
import com.example.news.util.DefaultPreferenceUtil
import com.example.superui.util.SuperDateUtil
import com.permissionx.guolindev.PermissionX

class SplashActivity : BaseLogicActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }

    }

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
                    prepareNext()
                } else {
                    finish()
                }
            }
    }

    private fun prepareNext() {
        Log.d(TAG, "pre")
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