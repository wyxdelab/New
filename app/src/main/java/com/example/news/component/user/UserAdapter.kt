package com.example.news.component.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemAdapter
import com.example.news.databinding.ItemTitleSmallBinding
import com.example.news.databinding.ItemUserBinding
import com.example.news.util.Constant
import com.example.news.util.ImageUtil

import org.apache.commons.lang3.StringUtils
import show

/**
 * 用户适配器
 */
class UserAdapter : BaseMultiItemAdapter<Any>() {
    init {
        //标题
        addItemType(Constant.STYLE_TITLE, object : OnMultiItemAdapterListener<Any, TitleVH> {
            override fun onBind(holder: TitleVH, position: Int, item: Any?) {
                val data = item as TitleData
                holder.binding.title.text = data.titleString
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): TitleVH {
                val viewBinding =
                    ItemTitleSmallBinding.inflate(LayoutInflater.from(context), parent, false)
                return TitleVH(viewBinding)
            }

        })

            //用户
            .addItemType(Constant.STYLE_USER, object : OnMultiItemAdapterListener<Any, UserVH> {
                override fun onBind(holder: UserVH, position: Int, item: Any?) {
                    val data = item as User
                    holder.bind(data)
                }

                override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): UserVH {
                    val viewBinding =
                        ItemUserBinding.inflate(LayoutInflater.from(context), parent, false)
                    return UserVH(viewBinding)
                }

            }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
                if (list[position] is TitleData) {
                    Constant.STYLE_TITLE
                } else {
                    Constant.STYLE_USER
                }
            }
    }

    /**
     * 标题VH
     */
    class TitleVH(val binding: ItemTitleSmallBinding) : RecyclerView.ViewHolder(binding.root)

    class UserVH(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) {
            ImageUtil.showAvatar(binding.icon, data.icon)
            binding.title.text = data.nickname
            binding.info.text = data.detail

            binding.info.show(StringUtils.isNotBlank(data.detail))
        }
    }
}