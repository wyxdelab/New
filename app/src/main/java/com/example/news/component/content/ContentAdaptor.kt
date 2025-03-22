package com.example.news.component.content

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.news.R
import com.example.news.databinding.ItemContentBinding
import com.example.news.util.ImageUtil
import com.example.superui.decoration.GridDividerItemDecoration
import com.example.superui.util.DensityUtil
import com.example.superui.util.SuperDateUtil
import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.lang3.StringUtils

class ContentAdaptor(val viewModel: ContentViewModel) : BaseQuickAdapter<Content, ContentAdaptor.ViewHolder>() {

    inner class ViewHolder(val binding: ItemContentBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * 绑定数据到局部文件，显示到屏幕
         */
        fun bindData(item: Content) {
            if (StringUtils.isNoneBlank(item.title)) {
                binding.content.text = item.title
            } else {
                binding.content.text = item.content
            }
            binding.nickname.text = binding.commentsCount.context.getString(R.string.comments_count, item.commentsCount)
            binding.date.text = SuperDateUtil.commonFormat(item.createdAt)
            //默认不显示视频容器
            binding.videoContainer.visibility = View.GONE
            //只有当确认有视频数据时才会显示视频容器
            if (StringUtils.isNoneBlank(item.uri)) {
                binding.videoContainer.visibility = View.VISIBLE
                ImageUtil.show(binding.icon, item.icons?.get(0))
                binding.duration.text = SuperDateUtil.s2ms(item.duration)
            } else if (CollectionUtils.isNotEmpty(item.icons)) {
                binding.list.visibility = View.VISIBLE

                var spanCount = 2
                if (item.icons!!.size >= 3) {
                    spanCount = 3
                }

                val layoutManager = GridLayoutManager(binding.list.context, spanCount)
                binding.list.layoutManager = layoutManager

                if (binding.list.itemDecorationCount > 0) {
                    binding.list.removeItemDecorationAt(0)
                }
                val itemDecoration = GridDividerItemDecoration(
                    binding.list.context,
                    DensityUtil.dip2px(binding.list.context, 5f).toInt()
                )

                binding.list.addItemDecoration(itemDecoration)

                var imageAdaptor = ImageAdaptor()

                binding.list.adapter = imageAdaptor
                imageAdaptor.submitList(item.icons!!)
            }
        }

    }

    /**
     * 绑定数据
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: Content?) {
        holder.bindData(item!!)
        holder.itemView.setOnClickListener {
            viewModel.itemClick(item)
        }
    }

    /**
     * 创建viewHolder
     */
    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemContentBinding.inflate(LayoutInflater.from(context), parent, false))
    }
}