package com.example.superui.util

import android.icu.util.Calendar

object SuperDateUtil {
    fun currentYear() : Int{
        /**
         * 当前年
         */
        return Calendar.getInstance().get(Calendar.YEAR)
    }
}