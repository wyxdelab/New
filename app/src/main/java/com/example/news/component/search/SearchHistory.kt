package com.example.news.component.search

import com.litesuits.orm.db.annotation.NotNull
import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.enums.AssignType

/**
 * 搜索历史模型
 * Table:指定这是一张表
 */
@Table("search_history")
class SearchHistory {
    /**
     * 标题
     * PrimaryKey:主键
     * BY_MYSELF:使用该字段的值
     * 还可以指定自动生成
     */
    @PrimaryKey(AssignType.BY_MYSELF)
    var content: String? = null

    /**
     * 创建时间
     *
     * @NotNull:不为空
     */
    @NotNull
    var createdAt: Long = 0
}