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
import com.example.news.fragment.BaseCommonFragment
import com.example.news.fragment.BaseDialogFragement
import com.example.news.util.DefaultPreferenceUtil
import com.example.superui.util.ScreenUtil
import com.example.superui.util.SuperProcessUtil
import com.example.superui.util.SuperTextUtil
import javax.crypto.KeyAgreement

/**
 * 服务条款和隐私协议对话框
 */
class TermServiceDialogFragment : BaseCommonFragment() {
    private lateinit var disagreeView: Button
    private lateinit var onAgreementClickListener: View.OnClickListener
    private lateinit var contentView:TextView
    private lateinit var primaryView:Button

    override fun initViews() {
        super.initViews()
        //保证点击其他地方，不会关闭对话框
        isCancelable = false

        //格式化文本中的特殊字符和链接
        contentView = findViewById<TextView>(R.id.content)
        SuperTextUtil.setLinkColor(contentView, getColor(requireContext(), R.color.link))

        primaryView = findViewById(R.id.primary)
        disagreeView = findViewById(R.id.disagree)

    }

    override fun initDatum() {
        super.initDatum()
        var content = Html.fromHtml(getString(R.string.term_service_privacy_content))
        contentView.setText(content)
    }


    override fun initListeners() {
        super.initListeners()
        primaryView.setOnClickListener {
            dismiss()
            onAgreementClickListener.onClick(it)
        }

        disagreeView.setOnClickListener{
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

    override fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_term_service, container, false)
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