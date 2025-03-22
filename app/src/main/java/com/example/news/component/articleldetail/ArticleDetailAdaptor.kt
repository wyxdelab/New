package com.example.news.component.articleldetail

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.component.comment.Comment
import com.example.news.util.ImageUtil
import com.example.superui.util.SuperDateUtil

class ArticleDetailAdaptor(val viewModel: ArticleDetailViewModel) : BaseQuickAdapter<Comment, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: Comment?) {
        item?.let {
            val iconView = holder.getView<ImageView>(R.id.icon)
            ImageUtil.showAvatar(iconView, item.user!!.icon)
            holder.setText(R.id.nickname, item.user!!.nickname)
            holder.setText(R.id.date, SuperDateUtil.commonFormat(item.createdAt))
            holder.setText(R.id.like_count, java.lang.String.valueOf(item.likesCount))

            //显示点赞状态
            if (item.isLiked()) {
                holder.setImageResource(R.id.like, R.drawable.thumb_selected)
                holder.setTextColorRes(R.id.like_count, R.color.primary)
            } else {
                holder.setImageResource(R.id.like, R.drawable.baseline_thumb)
                holder.setTextColorRes(R.id.like_count, R.color.black80)
            }

            var contentView = holder.getView<TextView>(R.id.content)
            holder.setText(R.id.content, item.content!!)

            if (item.commentsCount > 0) {
                holder.setText(
                    R.id.comments_count, context.getString(
                        R.string.reply_count,
                        item.commentsCount
                    )
                )
            } else {
                holder.setText(
                    R.id.comments_count, R.string.reply
                )
            }

            //回复按钮点击
            holder.getView<View>(R.id.comments_count_container)
                .setOnClickListener { viewModel.loadReplyComment(item) }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_comment, parent)
    }
}