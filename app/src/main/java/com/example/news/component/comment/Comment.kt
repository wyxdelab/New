package com.example.news.component.comment


import com.example.news.component.content.Content
import com.example.news.component.user.User
import com.example.news.entity.Common
import org.apache.commons.lang3.StringUtils


class Comment : Common() {
    var content: String? = null

    /**
     * 被回复评论id
     */
    var parentId: String? = null

    /**
     * 内容作者最新回复一条评论
     */
    val authorComment: Comment? = null

    /**
     * 视频id
     */
    val videoId: String? = null

    var articleId: String? = null

    private val article: Content? = null

    /**
     * 用户id
     */
    private val userId: String? = null

    /**
     * 用户
     */
    val user: User? = null

    /**
     * 点赞数
     */
    val likesCount: Long = 0
    val commentsCount: Long = 0

    /**
     * 点赞关系id
     *
     *
     * 有值表示点赞了
     */
    val likeId: String? = null

    /**
     * 图片列表
     * 逗号分割
     */
    val media: String? = null

//    constructor(id: String?, content: String?) : super(id) {
//        this.content = content
//    }


    /**
     * 是否点赞了
     *
     * @return
     */
    fun isLiked(): Boolean {
        return StringUtils.isNotBlank(likeId)
    }


}
