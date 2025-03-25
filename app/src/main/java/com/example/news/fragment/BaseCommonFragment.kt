package com.example.news.fragment

import android.content.Intent
import android.view.View
import androidx.annotation.IdRes
import com.example.news.util.Constant

abstract class BaseCommonFragment:BaseFragement() {
    fun <T : View?> findViewById(@IdRes id: Int): T {
        return requireView().findViewById(id)
    }

    /**
     * 获取int值
     */
    protected fun extraInt(key: String): Int {
        return requireArguments().getInt(key)
    }

    protected fun startActivityExtraId(
        clazz: Class<*>?,
        id: String
    ) {
        val intent = Intent(activity, clazz).apply {
            putExtra(Constant.ID, id)
        }

        startActivity(intent)
    }
}