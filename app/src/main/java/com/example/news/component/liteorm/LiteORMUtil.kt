package com.example.news.component.liteorm

import android.content.Context
import com.example.news.component.search.SearchHistory
import com.example.news.config.Config
import com.example.news.util.PreferenceUtil

import com.litesuits.orm.LiteOrm
import com.litesuits.orm.db.assit.QueryBuilder

/**
 * LiteOrm数据库工具类
 */
class LiteORMUtil private constructor(context: Context) {
    private val context: Context
    private val orm: LiteOrm

    init {
        this.context = context.applicationContext

        //创建数据库实例
        val databaseName = String.format("%s.db", PreferenceUtil.getUserId())
        orm = LiteOrm.newSingleInstance(this.context, databaseName)

        //设置调试模式
        orm.setDebugged(Config.DEBUG)
    }

    //region 搜索历史
    /**
     * 创建或更新搜索历史
     *
     * @param data
     */
    fun createOrUpdate(data: SearchHistory) {
        orm.save(data)
    }

    /**
     * 查询所有搜索历史
     *
     * @return
     */
    fun querySearchHistory(): List<SearchHistory> {
        val queryBuilder: QueryBuilder<SearchHistory> =
            QueryBuilder<SearchHistory>(SearchHistory::class.java)
                .appendOrderDescBy("createdAt")
        return orm.query(queryBuilder)
    }

    /**
     * 删除搜索历史
     *
     * @param data
     */
    fun deleteSearchHistory(data: SearchHistory) {
        orm.delete(data)
    }
    //endregion

//    fun saveSection(data: Section) {
//        orm.save(data)
//    }
//
//    fun querySection(data: String): Section? {
//        return orm.queryById(data, Section::class.java)
//    }

    companion object {
        private var instance: LiteORMUtil? = null

        @Synchronized
        fun getInstance(context: Context): LiteORMUtil {
            if (instance == null) {
                instance = LiteORMUtil(context)
            }
            return instance!!
        }

        fun destroy() {
            instance = null
        }
    }
}