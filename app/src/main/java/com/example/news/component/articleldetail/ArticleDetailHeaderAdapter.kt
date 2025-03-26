package com.example.news.component.articleldetail

import android.content.Context
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.example.news.R
import com.example.news.component.content.Content
import com.example.news.databinding.HeaderArticleDetailBinding
import com.example.news.util.ImageUtil
import com.example.news.util.RichUtil
import com.example.superui.util.SuperTextUtil
import show
import org.apache.commons.lang3.StringUtils
import timber.log.Timber

/**
 * 文章详情头部适配器
 */
class ArticleDetailHeaderAdapter(val viewModel: ArticleDetailViewModel) :
    BaseSingleItemAdapter<Content, ArticleDetailHeaderAdapter.VH>() {

    inner class VH(val binding: HeaderArticleDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            SuperTextUtil.setLinkColor(
                binding.content,
                ContextCompat.getColor(binding.content.context, R.color.link)
            )
        }

        fun bind(data: Content) {
            if (StringUtils.isNotBlank(data.title)) {
                binding.title.text = data.title
                binding.title.show()
            }

            binding.content.text = processContent(data.content!!)

            binding.commentCount.text =
                binding.content.context.getString(R.string.comments_count, data.commentsCount)

            binding.info.text =
                binding.content.context.getString(R.string.other_count, data.likesCount)

            //用户信息
            ImageUtil.showAvatar(binding.icon, data.user!!.icon)
            binding.nickname.text = data.user!!.nickname

            //归属地
            if (StringUtils.isNotBlank(data.province)) {
                binding.address.show()
                binding.address.text =
                    binding.root.context.getString(R.string.from_address, data.province)
            }

        }

        /**
         * 处理文本点击事件
         * 这部分可以用监听器回调到Activity中处理
         *
         * @param content
         * @return
         */
        private fun processContent(content: String): SpannableString {
            //设置点击事件

            //返回结果
            return RichUtil.processContent(binding.content.context,
                content,
                object : RichUtil.OnTagClickListener {
                    override fun onTagClick(data: String, matchResult: RichUtil.MatchResult) {
                        val clickText: String = RichUtil.removePlaceholderString(data)
                        Timber.d("processContent mention click %s", clickText)
//                        viewModel.jumpToUserDetail(clickText)
                    }
                },
                object : RichUtil.OnTagClickListener {
                    override fun onTagClick(data: String, matchResult: RichUtil.MatchResult) {
                        val clickText: String = RichUtil.removePlaceholderString(data)
                        Timber.d("processContent hashtag click %s", clickText)
                    }
                }
            )
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(HeaderArticleDetailBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, data: Content?) {
        data?.let {
            holder.bind(it)
        }
    }
}