package com.example.news.activity
/**
 * 本项目的通用逻辑，例如：背景颜色等
 */
open class BaseLogicActivity:BaseCommonActivity() {

    protected val hostActivity: BaseLogicActivity
        protected get() = this
}