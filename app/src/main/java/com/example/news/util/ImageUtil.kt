package com.example.news.util

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.news.AppContext
import com.example.news.R
import com.example.superui.util.DensityUtil

import java.io.FileInputStream
import java.io.InputStream

/**
 * 图片工具类
 */
object ImageUtil {
    /**
     * 显示本地图片
     *
     * @param view
     * @param data
     */
    fun showLocalImage(view: ImageView, data: String) {
        //获取通用配置
        val options: RequestOptions = getCommonRequestOptions()

        //使用Glide显示图片
        Glide.with(view.context)
            .load(data)
            .apply(options)
            .into(view!!)
    }


    /**
     * 显示头像
     */
    fun showAvatar(view: ImageView, data: String?, round: Boolean = false) {
        show(view, data, R.drawable.default_avatar, round)
    }

    /**
     * 显示网络图片
     */
    fun show(
        view: ImageView,
        data: String?,
        defaultRes: Int = R.drawable.placeholder,
        round: Boolean = false
    ) {
        if (TextUtils.isEmpty(data)) {
            //空

            //显示默认图片
            view.setImageResource(defaultRes)
        } else {
            val url = if (data!!.startsWith("http") || data.startsWith("/"))
            //已经是网络图片，或者本地路径
                data
            else
                ResourceUtil.resourceUri(data)

//            val glideUrl = GlideUrl(
//                url, LazyHeaders.Builder()
//                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.70")
//                    .build()
//            )

            //获取通用配置
//            val options: RequestOptions = getCommonRequestOptions(round)

            Glide.with(view).load(
                url
            ).apply {
                error(R.drawable.placeholder_error)
            }.into(view)

        }
    }

    /**
     * 获取公共配置
     *
     * @return
     */
    fun getCommonRequestOptions(round: Boolean = false): RequestOptions {
        //创建配置选项
        val options = RequestOptions()

        //占位图
//        options.placeholder(R.drawable.placeholder)

        //出错后显示的图片
        //包括：图片不存在等情况
        options.error(R.drawable.placeholder_error)

        //从中心裁剪
//        options.centerCrop()

        if (round) {
            options.transform(RoundedCorners(DensityUtil.dip2px(AppContext.instance, 5F).toInt()))
        }

        options.diskCacheStrategy(DiskCacheStrategy.ALL)
        return options
    }

//    /**
//     * 显示绝对路径图片
//     *
//     * @param context
//     * @param view
//     * @param data
//     */
//    fun showFull(view: ImageView, data: String) {
//        //获取功能配置
//        val options: RequestOptions = getCommonRequestOptions()
//
//        //显示图片
//        Glide.with(view)
//            .load(data)
//            .apply(options)
//            .into(view!!)
//    }
//
//    /**
//     * 获取图片宽高
//     */
//    fun getImageSize(data: String): IntArray {
//        val size = IntArray(2)
//
//        val `is`: InputStream = FileInputStream(data)
//
//        val onlyBoundsOptions = BitmapFactory.Options()
//        onlyBoundsOptions.inJustDecodeBounds = true
//
//        BitmapFactory.decodeStream(`is`, null, onlyBoundsOptions)
//
//        val originalWidth = onlyBoundsOptions.outWidth
//        val originalHeight = onlyBoundsOptions.outHeight
//        size[0] = originalWidth
//        size[1] = originalHeight
//
//        return size
//    }

}