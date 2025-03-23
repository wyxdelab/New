package com.example.news.util

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * 文件工具类
 */
object FileUtil {
    /**
     * 获取缓存广告目录
     *
     * @param context
     * @param data
     * @return
     */
    fun adFile(context: Context, data: String): File {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + "/ads",
            data
        )
        val parentFile = file.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        return file
    }

    /**
     * 文件大小格式化
     *
     * @param value 文件大小；单位byte
     * @return 格式化的文件大小；例如：1.22M
     */
    fun formatFileSize(value: Long): String {
        if (value > 0) {
            val size = value.toDouble()
            val kiloByte = size / 1024
            if (kiloByte < 1 && kiloByte > 0) {
                //不足1k
                //return  size+"Byte";
                return String.format("%.2fByte", size)
            }
            val megaByte = kiloByte / 1024
            if (megaByte < 1) {
                //不足1M
                return String.format("%.2fK", kiloByte)
            }
            val gigaByte = megaByte / 1024
            if (gigaByte < 1) {
                //不足1G
                return String.format("%.2fM", megaByte)
            }
            val teraByte = gigaByte / 1024
            return if (teraByte < 1) {
                //不足1T
                String.format("%.2fG", gigaByte)
            } else String.format("%.2fT", teraByte)
        }
        return "0K"
    }
}