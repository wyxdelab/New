package com.example.news.fragment

import android.view.View
import androidx.annotation.IdRes

abstract class BaseCommonDialogFragment:BaseDialogFragement() {
    fun <T : View?> findViewById(@IdRes id: Int): T {
        return requireView().findViewById(id)
    }
}