package com.example.news.util

import cn.hutool.extra.pinyin.PinyinUtil
import com.example.news.component.category.Category
import com.example.news.component.user.TitleData
import com.example.news.component.user.User
import com.example.news.component.user.UserResult
import com.google.common.collect.Ordering


/**
 * 数据工具类
 */
object DataUtil {
    val categories: List<Category> = listOf(
        Category.create(
            "1", "推荐"
        ), Category.create(
            "2", "发现"
        ), Category.create(
            "3", "每日必看"
        ), Category.create(
            "4", "视频"
        ), Category.create(
            "5", "小说"
        ), Category.create(
            "6", "问答"
        ), Category.create(
            "7", "音乐"
        )
    )

//    val sections: List<Section> = listOf(
//        Section(
//            "1",
//            "实现通用WebView功能",
//            listOf(
//                Section("100","Toolbar使用方法和封装", "news/v3.mp4", 150),
//                Section("101","实现WebView进度条功能", "v1.mp4", 80),
//                Section("102","实现打开第三方应用界面", "news/news_3.mp4", 190),
//                Section("103","隐式意图打开应用内界面", "v2.mp4", 780),
//                Section("104","企业级日志解决方案简介", "news/news_3.mp4", 230),
//                Section("105","第三方日志框架集成和使用", "news/news_3.mp4", 278)
//            )
//        ),
//        Section(
//            "2",
//            "用户登录和注册功能实现", listOf(
//                Section("201","登录注册效果和实现分析", "news/news_3.mp4", 150),
//                Section("202","实现用户登录首页界面", "news/news_3.mp4", 230),
//                Section("203","实现用户名和密码登陆", "news/news_3.mp4", 190),
//                Section("204","如何实现关闭多个界面", "news/news_3.mp4", 780),
//                Section("205","实现用户退出弹窗提示", "news/news_3.mp4", 230),
//                Section("206","实现用户注册界面逻辑", "news/news_3.mp4", 278)
//            )
//        ),
//        Section(
//            "3",
//            "实现通过验证码登陆功能", listOf(
//                Section("300","Android Studio调试项目", "news/news_3.mp4", 150),
//                Section("301","输入用户标识界面和功能", "news/news_3.mp4", 450),
//                Section("302","实现输入验证码界面和功能", "news/news_3.mp4", 190),
//                Section("303","如何实现发送验证码功能", "news/news_3.mp4", 780),
//                Section("304","实现验证码重置密码功能", "news/news_3.mp4", 230)
//            )
//        )
//    )

//    fun getTestCourseData(): Course {
//        return Course().apply {
//            title = "Android云音乐MVVM+Jetpack"
//            icon = "news/ab66215f7f012b1c72661ed207ff849d.png"
//            sections = this@DataUtil.sections
//
//            price = 69900
//        }
//    }
//
    //region 用户
    /**
     * 返回用户测试数据
     *
     * @return
     */
    fun getTestUserData(): List<User> {
        //创建一个列表
        val results: ArrayList<User> = ArrayList<User>()

        //全中文
        var user: User? = null
        for (i in 0..49) {
            user = User()
            user.nickname = "我的云音乐$i"
            results.add(user)
        }

        //有单词
        for (i in 0..49) {
            user = User()
            user.nickname = "爱学啊smile$i"
            results.add(user)
        }

        //全中文
        for (i in 0..49) {
            user = User()
            user.nickname = "爱学啊李薇$i"
            results.add(user)
        }

        //全英文
        for (i in 0..49) {
            user = User()
            user.nickname = "Jack li$i"
            results.add(user)
        }
        return results
    }

    /**
     * 根据用户昵称计算出拼音
     *
     * @param datum
     * @return
     */
    fun processUserPinyin(datum: List<User>): List<User> {
        //循环所有数据
        for (data in datum) {
            //获取全拼
            data.pinyin = PinyinUtil.getPinyin(data.nickname, "").lowercase()

            //获取拼音首字母
            //例如："爱学啊"
            //结果为：axa
            data.pinyinFirst = PinyinUtil.getFirstLetter(data.nickname, "").lowercase()

            //拼音首字母的首字母
            //例如："爱学啊"
            //结果为：a
            data.first = data.pinyinFirst!!.substring(0, 1)
        }
        return datum
    }

    /**
     * 根据用户首字母分组
     *
     *
     * 标题
     * 真实数据
     * 标题
     * 真实数据
     *
     * @param datum
     * @return
     */
    fun processUser(datum: List<User>): UserResult {
        //创建结果数组
        var datum: List<User> = datum
        val results: MutableList<Any> = ArrayList<Any>()

        //创建字母列表
        val letters = ArrayList<String>()

        //字母索引列表
        val indexes = ArrayList<Int>()

        //按照第一个字母排序
        //这里使用的Guava提供的排序方法
        //也可以使用Java的排序方法
        val byFirst: Ordering<User> = object : Ordering<User>() {
            override fun compare(left: User, right: User): Int {
                //根据第一个字母排序
                return left.first!!.compareTo(right.first!!)
            }
        }

        //排序
        datum = byFirst.immutableSortedCopy<User>(datum)

        //按照拼音首字母的第一个字母分组
        //这些操作都可以使用响应式编程方法
        //这里为了简单使用了最普通的方法
        //因为只要明白了原理
        //使用其他方法就是语法不同而已

        //循环所有数据

        //上一次用户
        var lastUser: User? = null
        for (data in datum) {
            if (lastUser != null && lastUser.first.equals(data.first)) {
                //相等
            } else {
                //添加标题
                val letter: String = data.first!!.uppercase()

                //添加字母索引
                indexes.add(results.size)

                //添加标题
                results.add(TitleData(letter))

                //添加字母
                letters.add(letter)
            }

            //添加用
            results.add(data)
            lastUser = data
        }

        //字母索引转为数组
        val indexArray = indexes.toTypedArray()
        return UserResult(results, letters, indexArray)
    }

    /**
     * 过滤用户数据
     *
     * @param datum
     * @param query
     */
    fun filterUser(datum: List<User>, query: String): List<User> {
        //创建列表
        val results: java.util.ArrayList<User> = java.util.ArrayList<User>()

        //遍历所有数据
        for (data in datum) {
            if (isMatchUser(data, query)) {
                //如果匹配

                //就添加到列表
                results.add(data)
            }
        }

        //返回结果
        return results
    }

    /**
     * 用户是否匹配给定的关键字
     *
     * @param data
     * @param query
     * @return
     */
    private fun isMatchUser(data: User, query: String): Boolean {
        //nickname是否包含搜索的字符串
        //全拼是否包含
        //首字母是否包含

        //如果需要更多的条件可以在加
        //条件越多
        //就更容易搜索到
        //但结果就越多
        return data.nickname!!.lowercase().contains(query) ||
                data.pinyin!!.contains(query) ||
                data.pinyinFirst!!.contains(query)
    }
    //endregion
}