package com.example.news.component.splash

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.FragmentManager
import com.example.news.R
import com.example.news.databinding.FragmentDialogTermServiceBinding
import com.example.news.fragment.BaseCommonDialogFragment
import com.example.news.fragment.BaseViewModelDialogFragment
import com.example.superui.util.ScreenUtil
import com.example.superui.util.SuperProcessUtil
import com.example.superui.util.SuperTextUtil

/**
 * 服务条款和隐私协议对话框
 */
class TermServiceDialogFragment : BaseViewModelDialogFragment<FragmentDialogTermServiceBinding>() {
    private lateinit var onAgreementClickListener: View.OnClickListener


    override fun initViews() {
        super.initViews()
        //保证点击其他地方，不会关闭对话框
        isCancelable = false

        //格式化文本中的特殊字符和链接
        SuperTextUtil.setLinkColor(binding.content, getColor(requireContext(), R.color.link))


    }

    override fun initDatum() {
        super.initDatum()
        var content = Html.fromHtml(getString(R.string.term_service_privacy_content))
        binding.content.text = content
    }


    override fun initListeners() {
        super.initListeners()
        binding.primary.setOnClickListener {
            dismiss()
            onAgreementClickListener.onClick(it)
        }

        binding.disagree.setOnClickListener{
            dismiss()
            SuperProcessUtil.killApp()
        }
    }
    override fun onResume() {
        super.onResume()
        //修改宽度，默认比AlertDialog.Builder显示对话框宽度窄，看着不好看
        //参考：https://stackoverflow.com/questions/12478520/how-to-set-dialogfragments-width-and-height
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ((ScreenUtil.getScreenWith(requireContext()) * 0.9).toInt())
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }


    companion object {
        /**
         * 显示对话框
         */
        fun show(fragmentManager: FragmentManager, onAgreementClickListener: OnClickListener) {
            var termServiceDialogFragment = TermServiceDialogFragment()
            termServiceDialogFragment.onAgreementClickListener = onAgreementClickListener
            termServiceDialogFragment.show(fragmentManager, "TermServiceDialogFragment")

        }
    }
}