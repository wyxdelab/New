package com.example.news.component.content

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.news.R
import com.example.news.util.ImageUtil
import com.luck.picture.lib.entity.LocalMedia

/**
 * 图片Adapter
 */
class ImageAdaptor: BaseQuickAdapter<Any, QuickViewHolder>() {
    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, data: Any?) {
        data?.let {
            val iconView = holder.getView<ImageView>(R.id.icon)
            when (data) {
                is String -> {
                    ImageUtil.show(iconView, data)
                }

                is LocalMedia -> {
                    //选择的图片
                    ImageUtil.showLocalImage(iconView, data.availablePath)

                    //显示删除按钮
                    holder.setGone(R.id.close, false)
                }

                else -> {
                    iconView.setImageResource(data as Int)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ): QuickViewHolder {
        return QuickViewHolder(R.layout.item_image, parent)
    }
}