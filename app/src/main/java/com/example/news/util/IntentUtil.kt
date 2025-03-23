package com.example.news.util

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * intent相关工具类
 */
object IntentUtil {
    /**
     * 将原来的intent上的自定义信息拷贝到新的intent
     *
     *
     * 主要是实现这样的功能
     * 例如：点击推送后会启动 启动界面并携带一些数据，而这些数据需要完全传递到MainActivity使用
     * 所以需要从启动界面的intent中获取并设置到启动主界面的intent
     *
     * @param oldIntent
     * @param intent
     * @return
     */
    fun cloneIntent(oldIntent: Intent, intent: Intent) {
        if (Intent.ACTION_MAIN != oldIntent.action) {
            //获取原来的信息，通常是应用中启动 该界面设置的，例如：MusicControlReceiver中实现的点击封面启动应用
            intent.action = oldIntent.action

            intent.putExtra(Constant.STYLE, oldIntent.getIntExtra(Constant.STYLE, 0))

            oldIntent.getStringExtra(Constant.ARTICLE_ID)?.let {
                intent.putExtra(Constant.ARTICLE_ID, it)
            }

//            intent.putExtra(Constant.PUSH, oldIntent.getStringExtra(Constant.PUSH))
        }
    }

    /**
     * 使用系统浏览器打开
     *
     * @param data
     */
    fun startBrowser(context: Context, data: String?) {
        val uri = Uri.parse(data)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(intent)
    }
}