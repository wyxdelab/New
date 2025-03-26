package com.example.superui.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object SuperKeyboardUtil {
    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    fun hideKeyboard(activity: Activity, edit: EditText) {
        //获取输入管理器
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //隐藏软键盘
        val windowToken = edit.windowToken
        if (windowToken != null) {
            inputMethodManager.hideSoftInputFromWindow(
                windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    fun hideKeyboard(activity: Activity) {
        //获取输入管理器
        val inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //隐藏软键盘
        inputMethodManager.hideSoftInputFromWindow(
            activity
                .currentFocus!!
                .getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}