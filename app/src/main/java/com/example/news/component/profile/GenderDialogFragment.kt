package com.example.news.component.profile

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.news.R

/**
 * 选择性别对话框
 */
class GenderDialogFragment : DialogFragment() {
    /**
     * 选择索引
     */
    private var selectedIndex = 0

    /**
     * 选择了监听器
     */
    private var onClickListener: DialogInterface.OnClickListener? = null

    /**
     * 创建对话框
     *
     * @param savedInstanceState
     * @return
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity()) //设置标题
            .setTitle(R.string.select_gender) //设置单选按钮
            .setSingleChoiceItems(R.array.dialog_gender, selectedIndex, onClickListener)
            .create()
    }

    companion object {
        /**
         * 显示
         *
         * @param fragmentManager
         * @param selectedIndex
         * @param onClickListener
         */
        fun show(
            fragmentManager: FragmentManager,
            selectedIndex: Int,
            onClickListener: DialogInterface.OnClickListener
        ) {
            //创建fragment
            val fragment = newInstance()

            //选择索引
            fragment.selectedIndex = selectedIndex

            //回调监听器
            fragment.onClickListener = onClickListener

            //显示
            fragment.show(fragmentManager, "GenderDialogFragment")
        }

        /**
         * 创建方法
         *
         * @return
         */
        fun newInstance(): GenderDialogFragment {
            val args = Bundle()
            val fragment = GenderDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}