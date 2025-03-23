package com.example.superui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.FragmentManager
import com.example.news.R
import com.example.news.fragment.BaseDialogFragement
import org.apache.commons.lang3.StringUtils
import show

/**
 * 对话框封装
 */
class SuperDialog private constructor(private val fragmentManager: FragmentManager) :
    BaseDialogFragement() {
    private var isShowCancelButton = true

    /**
     * 标题
     */
    private var title: String? = null
    private var titleRes = 0

    /**
     * 提示内容
     */
    private var message: String? = null
    private var messageRes = 0

    /**
     * 确认按钮颜色
     */
    private var confirmButtonColorRes = 0

    /**
     * 确认按钮文本
     */
    private var confirmButtonTextRes = 0

    /**
     * 取消按钮文本
     */
    private var cancelButtonTextRes = 0

    /**
     * 确认点击回调方法
     */
    private var onClickListener: View.OnClickListener? = null

    /**
     * 取消回调方法
     */
    private var onCancelClickListener: View.OnClickListener? = null

    /**
     * 要显示的布局
     */
    private val layoutResource = R.layout.super_dialog
    private lateinit var titleView: TextView
    private lateinit var messageView: TextView
    private lateinit var cancelView: TextView
    private lateinit var confirmView: TextView
    private lateinit var inputView: EditText

    /**
     * 是否显示输入框
     */
    private var isShowInput = false
    override fun initViews() {
        super.initViews()
        titleView = findViewById<TextView>(R.id.title)
        messageView = findViewById<TextView>(R.id.message)
        inputView = findViewById<EditText>(R.id.input)
        cancelView = findViewById<TextView>(R.id.cancel)
        confirmView = findViewById<TextView>(R.id.confirm)
    }

    override fun initDatum() {
        super.initDatum()
        isCancelable = false

        //设置数据
        titleView!!.text = if (title != null) title else getString(titleRes)
        if (StringUtils.isNotBlank(message) || messageRes != 0) {
            messageView!!.visibility = View.VISIBLE
            messageView!!.text = if (message != null) message else getString(messageRes)
        } else {
            messageView!!.visibility = View.GONE
        }
        inputView.show(isShowInput)
        cancelView!!.visibility = if (isShowCancelButton) View.VISIBLE else View.GONE
        if (confirmButtonColorRes != 0) {
            confirmView!!.setTextColor(getColor(requireContext(), confirmButtonColorRes))
        }
        if (confirmButtonTextRes != 0) {
            confirmView!!.setText(confirmButtonTextRes)
        }
        if (cancelButtonTextRes != 0) {
            cancelView!!.setText(cancelButtonTextRes)
        }
    }

    override fun initListeners() {
        super.initListeners()
        //取消按钮点击
        cancelView!!.setOnClickListener { v ->
            dismiss()
            if (onCancelClickListener != null) {
                onCancelClickListener!!.onClick(v)
            }
        }

        //确认按钮点击
        confirmView!!.setOnClickListener { v ->
            dismiss()
            if (onClickListener != null) {
                onClickListener!!.onClick(v)
            }
        }
    }

    /**
     * 显示对话框
     *
     * @return
     */
    fun show(): SuperDialog {
        show(fragmentManager, "SuperDialog")
        return this
    }

    /**
     * 配置删除样式
     * 确认按钮颜色为红色，文本更改为删除
     */
    fun deleteStyle(): SuperDialog {
        confirmButtonColorRes = R.color.warning
        confirmButtonTextRes = R.string.delete
        return this
    }

    /**
     * 显示提示样式
     * 只有确认按钮
     */
    fun alertStyle(): SuperDialog {
        isShowCancelButton = false
        return this
    }

    /**
     * 标题，输入框，确认按钮
     *
     * @return
     */
    fun titleInputConfirmStyle(): SuperDialog {
        isShowInput = true
        return this
    }

    fun setOnClickListener(onClickListener: View.OnClickListener?): SuperDialog {
        this.onClickListener = onClickListener
        return this
    }

    fun setOnClickListener(onClickListener: View.OnClickListener?, titleRes: Int): SuperDialog {
        this.onClickListener = onClickListener
        confirmButtonTextRes = titleRes
        return this
    }

    fun setOnCancelClickListener(
        onCancelClickListener: View.OnClickListener?,
        titleRes: Int
    ): SuperDialog {
        this.onCancelClickListener = onCancelClickListener
        cancelButtonTextRes = titleRes
        return this
    }

    fun setTitle(title: String?): SuperDialog {
        this.title = title
        return this
    }

    fun setTitleRes(titleRes: Int): SuperDialog {
        this.titleRes = titleRes
        return this
    }

    fun setCancelButtonTextRes(data: Int): SuperDialog {
        cancelButtonTextRes = data
        return this
    }

    fun setMessage(message: String?): SuperDialog {
        this.message = message
        return this
    }

    fun setMessageRes(messageRes: Int): SuperDialog {
        this.messageRes = messageRes
        return this
    }

    override fun getLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return layoutInflater.inflate(layoutResource, null)
    }

    companion object {
        fun newInstance(fragmentManager: FragmentManager): SuperDialog {
            val args = Bundle()
            val fragment = SuperDialog(fragmentManager)
            fragment.arguments = args
            return fragment
        }
    }
}