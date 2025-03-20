package com.example.superui.util

import android.os.Process

object SuperProcessUtil {
    fun killApp() {
        Process.killProcess(Process.myPid())
    }
}