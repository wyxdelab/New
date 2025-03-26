package com.example.news.util

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.example.news.R
import com.example.superui.text.SuperClickableSpan
import java.util.regex.Pattern

object RichUtil {
    const val MENTION = "@"

    /**
     * hashTag开始
     */
    const val HAST_TAG = "#"

    /**
     * 匹配mention的正则表达式
     * 详细的请参考《详解正则表达式》课程
     */
    private const val REG_MENTION = "(@[\\u4e00-\\u9fa5a-zA-Z0-9_-]{1,30})"

    /**
     * 匹配hashTag的正则表达式
     * ？表示禁用贪婪模式
     */
    private const val REG_HASH_TAG = "(#.*?#)"

    fun findMentions(data: String): List<MatchResult> {
        return find(REG_MENTION, data)
    }

    fun findHash(data: String): List<MatchResult> {
        return find(REG_HASH_TAG, data)
    }

    /**
     * 正则表达式查找
     *
     * @param reg  正则表达式
     * @param data 被查找的数据
     * @return
     */
    private fun find(reg: String, data: String): List<MatchResult> {
        //创建结果列表
        val results = mutableListOf<MatchResult>()

        //编译正则表达式
        val pattern = Pattern.compile(reg)

        //通过正则表达式匹配内容
        val matcher = pattern.matcher(data)
        while (matcher.find()) {
            //将开始位置
            //结束位置
            //保存到一个类中
            val matchResult =
                MatchResult(matcher.start(), matcher.end(), matcher.group(0).trim())
            results.add(matchResult)
        }

        return results
    }

    /**
     * 处理文本添加点击事件
     *
     * @param context
     * @param data
     * @return
     */
    fun processContent(
        context: Context,
        data: String,
        onMentionClickListener: OnTagClickListener,
        onHashTagClickListener: OnTagClickListener
    ): SpannableString {
        //创建结果字符串
        //这个就是Android中的富文本字符串
        val result = SpannableString(data)

        //查找@
        var tags = findMentions(data)

        for (matchResult in tags) {
            processInner(result, matchResult, onMentionClickListener)
        }

        //匹配话题
        tags = findHash(data)

        //遍历所有数据并处理
        for (matchResult in tags) {
            processInner(result, matchResult, onHashTagClickListener)
        }

        //返回结果
        return result
    }

    private fun processInner(
        result: SpannableString,
        matchResult: MatchResult,
        tagClickListener: OnTagClickListener
    ) {
        result.setSpan(object : SuperClickableSpan() {
            override fun onClick(widget: View) {
                tagClickListener.onTagClick(matchResult.content, matchResult)
            }
        }, matchResult.start, matchResult.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    /**
     * 移除字符串中首的@
     * 移除首尾的#
     *
     * @param data
     * @return
     */
    fun removePlaceholderString(data: String): String {
        if (data.startsWith(MENTION)) {
            //@人字符串

            //从1开始截取到末尾
            return data.substring(1)
        } else if (data.startsWith(HAST_TAG)) {
            //话题字符串

            //截取1~最后一个字符串
            return data.substring(1, data.length - 1)
        }

        //如果不满足格式
        //就直接返回
        return data
    }

    /**
     * 文本进行高亮
     * 不添加点击事件
     *
     * @param context
     * @param data
     * @return
     */
    fun processHighlight(context: Context, data: String): SpannableString {
        var mentionsAndHashTags = mutableListOf<MatchResult>()

        //查找@
        mentionsAndHashTags.addAll(findMentions(data))

        //匹配话题
        mentionsAndHashTags.addAll(findHash(data))

        //创建结果字符串
        //这个就是Android中的富文本字符串
        val result = SpannableString(data)

        //遍历所有数据并处理
        for (matchResult in mentionsAndHashTags) {
            //高亮文本
            val span = ForegroundColorSpan(context.getColor(R.color.link))

            //设置span
            //SPAN_EXCLUSIVE_EXCLUSIVE:不包括开始结束位置
            result.setSpan(
                span,
                matchResult.start,
                matchResult.end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return result
    }

    interface OnTagClickListener {
        /**
         * 点击回调方法
         *
         * @param data        点击的内容
         * @param matchResult 点击范围
         */
        fun onTagClick(data: String, matchResult: MatchResult)
    }

    /**
     * 匹配的结果
     */
    class MatchResult
    /**
     * 构造方法
     *
     * @param start
     * @param end
     * @param content
     */(
        /**
         * 开始位置
         */
        var start: Int,
        /**
         * 结束位置
         */
        var end: Int,
        /**
         * 匹配到的内容
         */
        var content: String
    )
}