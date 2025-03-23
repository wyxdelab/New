package com.example.news.component.ad

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.activity.BaseViewModelActivity
import com.example.news.component.main.MainActivity
import com.example.news.databinding.ActivityAdBinding
import com.example.news.util.Constant
import com.example.news.util.FileUtil
import com.example.news.util.ImageUtil
import com.example.news.util.IntentUtil
import com.example.news.util.PreferenceUtil
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import show
import java.io.File

/**
 * 广告启动界面
 */
class AdActivity : BaseViewModelActivity<ActivityAdBinding>() {
    private var adCountDownTimer: CountDownTimer? = null
    private var data: Ad? = null
    private var action: String? = null
    protected override fun initViews() {
        super.initViews()
        //设置沉浸式状态栏
        QMUIStatusBarHelper.translucent(this)
    }

    protected override fun initDatum() {
        super.initDatum()

        //获取广告信息
        data = PreferenceUtil.getSplashAd()
        if (data == null) {
            next()
            return
        }

        //显示广告信息
        show()
        binding.shimmer.startShimmer()
    }

    private fun show() {
        val targetFile: File = FileUtil.adFile(hostActivity, data!!.icon!!)
        if (!targetFile.exists()) {
            //记录日志，因为正常来说，只要保存了，文件不能丢失
            next()
            return
        }
        binding.adControl.show()
        when (data!!.style) {
            Constant.VALUE0 -> showImageAd(targetFile)
//            Constant.VALUE10 -> showVideoAd(targetFile)
        }
    }
    /**
     * 显示图片广告
     *
     * @param data
     */
    private fun showImageAd(data: File) {
        ImageUtil.showLocalImage(binding.image, data.absolutePath)
        startCountDown(5000)
    }

    private fun startCountDown(data: Int) {
        //创建倒计时
        adCountDownTimer = object : CountDownTimer(data.toLong(), 1000) {
            /**
             * 每次间隔调用
             *
             * @param millisUntilFinished
             */
            override fun onTick(millisUntilFinished: Long) {
                binding.skip.setText(
                    getString(
                        R.string.skip_ad_count,
                        millisUntilFinished / 1000 + 1
                    )
                )
            }

            /**
             * 倒计时完成
             */
            override fun onFinish() {
                //执行下一步方法
                next()
            }
        }

        //启动定时器
        adCountDownTimer!!.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelCountDown()
    }

    private fun cancelCountDown() {
        if (adCountDownTimer != null) {
            adCountDownTimer!!.cancel()
            adCountDownTimer = null
        }
    }
    private fun next() {
        val intent = Intent(hostActivity, MainActivity::class.java)
        IntentUtil.cloneIntent(getIntent(), intent)
        if (data != null) {
            //添加广告
            intent.putExtra(Constant.AD, data)
        }
        if (action != null) {
            //要跳转到广告界面
            //先启动主界面的
            //好处是
            //用户在广告界面
            //返回正好看到的主界面
            //这样才符合应用逻辑
            intent.action = action
        }
        startActivity(intent)

        //关闭当前界面
        finish()
    }

    override fun initListeners() {
        super.initListeners()
        //跳过广告按钮
        binding.skip.setOnClickListener(View.OnClickListener { //取消倒计时
            cancelCountDown()
            next()
//            AnalysisUtil.onSkipAd(hostActivity, PreferenceUtil.getUserId())
        })

        //点击广告按钮
        binding.primary.setOnClickListener(View.OnClickListener { //取消倒计时
            cancelCountDown()
            action = Constant.ACTION_AD
            next()
        })
    }
}