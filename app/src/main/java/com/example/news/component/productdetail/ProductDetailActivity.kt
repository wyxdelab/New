package com.example.news.component.productdetail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.activity.BaseViewModelActivity
import com.example.news.component.product.Product
import com.example.news.databinding.ActivityProductDetailBinding
import com.example.news.util.ImageUtil
import com.example.news.util.ResourceUtil
import com.example.superui.util.ScreenUtil
import com.example.superui.util.SuperDarkUtil
import com.example.superui.util.SuperTextUtil
import com.ixuea.courses.mymusic.component.productdetail.ProductDetailViewModel
import com.ixuea.courses.mymusic.component.productdetail.ProductDetailViewModelFactory
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import kotlinx.coroutines.launch
import me.wcy.htmltext.HtmlImageLoader
import me.wcy.htmltext.HtmlText
import me.wcy.htmltext.OnTagClickListener
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class ProductDetailActivity : BaseViewModelActivity<ActivityProductDetailBinding>() {
    private lateinit var viewModel: ProductDetailViewModel
    private var colorPrimary = 0
    private var textColor = 0
    private var colorBackground = 0

    override fun initViews() {
        super.initViews()
        //状态栏透明，内容显示到状态栏
        QMUIStatusBarHelper.translucent(this)

        colorPrimary = ResourceUtil.getColorAttributes(
            hostActivity,
            com.google.android.material.R.attr.colorPrimary
        )
        textColor = ResourceUtil.getColorAttributes(
            hostActivity,
            com.google.android.material.R.attr.colorOnSurface
        )
        colorBackground =
            ResourceUtil.getColorAttributes(hostActivity, android.R.attr.colorBackground)

        //原价设置删除线
        binding!!.originPrice.paintFlags =
            binding!!.originPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        //创建通用指示器
        val commonNavigator = CommonNavigator(hostActivity)

        //设置适配器
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            /**
             * 指示器数量
             *
             * @return
             */
            override fun getCount(): Int {
                return indicatorTitles.size
            }

            /**
             * 返回当前位置的标题
             *
             * @param context
             * @param index
             * @return
             */
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                //创建简单的文本控件
                val titleView: SimplePagerTitleView = SimplePagerTitleView(context)

                //默认颜色
                titleView.normalColor = colorPrimary

                //选中后的颜色
                titleView.selectedColor = textColor

                //设置显示的文本
                titleView.setText(indicatorTitles.get(index))

                //点击回调监听
                titleView.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View) {}
                })
                return titleView
            }

            /**
             * 返回指示器
             * 就是下面那条线
             *
             * @param context
             * @return
             */
            override fun getIndicator(context: Context): IPagerIndicator {
                //创建一条线
                val indicator: LinePagerIndicator = LinePagerIndicator(context)

                //线的宽度和内容一样
                indicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT

                //高亮颜色
                indicator.setColors(getColor(R.color.primary))
                return indicator

                //返回null表示不显示指示器
                //return null;
            }
        }

        //如何位置显示不下指示器
        //是否自动调整
        commonNavigator.isAdjustMode = true

        //设置导航器
        binding!!.indicator.navigator = commonNavigator
        SuperTextUtil.setLinkColor(binding!!.detail, getColor(R.color.link))
    }

    override fun initDatum() {
        super.initDatum()

        //创建ViewModel
        val viewModelFactory = ProductDetailViewModelFactory(extraId())
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(ProductDetailViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    showData(data)
                }
        }

//        lifecycleScope.launch {
//            viewModel.purchasePage
//                .collect { data ->
//                    startActivityExtraId(
//                        ConfirmOrderActivity::class.java,
//                        data
//                    )
//                }
//        }
//
//        lifecycleScope.launch {
//            viewModel.stock
//                .collect { data ->
//                    showStock(data)
//                }
//        }

        viewModel.loadData()
    }

    override fun initListeners() {
        super.initListeners()
        binding.back.setOnClickListener {
            finish()
        }

        //监听列表滚动，目的是显示标题栏
        binding!!.scrollContent.setOnScrollChangeListener(View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            Timber.d("onScrollChange %d %d", scrollY, oldScrollY)
            var alpha = scrollY
            if (alpha > 255) {
                alpha = 255
            }

            //创建一个白色，默认是完全透明，向上滚动，慢慢变为完全不透明
            val toolbarBackgroundColor: Int
            if (SuperDarkUtil.isDark(hostActivity)) {
                //深色模式；完全不透明后就是黑色
                toolbarBackgroundColor = Color.argb(alpha, 0, 0, 0)
            } else {
                //创建一个白色，默认是完全透明，向上滚动，慢慢变为完全不透明
                toolbarBackgroundColor = Color.argb(alpha, 255, 255, 255)
            }
            binding!!.toolbarContainer.setBackgroundColor(toolbarBackgroundColor)
            binding!!.indicator.alpha = (alpha / 255.0).toFloat()
        })

        //添加到购物车按钮点击
//        binding.addCart.setOnClickListener {
//            viewModel.addCart()
//        }
//
//        binding.primary.setOnClickListener {
//            viewModel.confirmOrder()
//        }
//
//        binding.share.setOnClickListener {
//            ShareImageActivity.start(hostActivity, viewModel.product)
//        }
    }

    private fun showData(data: Product) {
        //轮播图
        binding.banner.setAdapter(object : BannerImageAdapter<String>(data.icons) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: String,
                position: Int,
                size: Int
            ) {
                //图片加载自己实现
                ImageUtil.show(holder.itemView as ImageView, data)
            }
        })
            .isAutoLoop(false)
            .addBannerLifecycleObserver(this).indicator = CircleIndicator(hostActivity)

        //价格，也可以在代码中格式化
        binding!!.price.text = java.lang.String.format("￥%.2f", data.priceFloat)
        binding!!.title.text = data.title
        binding!!.highlight.text = data.highlight

        //购买人数
        binding!!.buyCount.text =
            resources.getString(R.string.buy_count, data.ordersCount)

        //详情
        HtmlText.from(data.detail)
            .setImageLoader(object : HtmlImageLoader {
                override fun loadImage(url: String, callback: HtmlImageLoader.Callback) {
                    Glide.with(hostActivity)
                        .asBitmap()
                        .load(url)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                callback.onLoadComplete(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                callback.onLoadFailed()
                            }
                        })
                }

                override fun getDefaultDrawable(): Drawable {
                    return (ContextCompat.getDrawable(
                        hostActivity,
                        R.drawable.placeholder
                    ))!!
                }

                override fun getErrorDrawable(): Drawable {
                    return (ContextCompat.getDrawable(
                        hostActivity,
                        R.drawable.placeholder_error
                    ))!!
                }

                override fun getMaxWidth(): Int {
                    return ScreenUtil.getScreenWith(hostActivity)
                }

                override fun fitWidth(): Boolean {
                    return true
                }
            })
            .setOnTagClickListener(object : OnTagClickListener {
                override fun onImageClick(
                    context: Context,
                    imageUrlList: List<String>,
                    position: Int
                ) {
                    // image click
                }

                override fun onLinkClick(context: Context, url: String) {
                    // link click
//                    Timber.d("onLinkClick %s", url)
                }
            })
            .into(binding!!.detail)
    }

    companion object {
        /**
         * 指示器标题
         */
        private val indicatorTitles = intArrayOf(
            R.string.product,
            R.string.detail
        )
    }
}