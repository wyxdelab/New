package com.example.superui.dropdown

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.news.R
import com.example.superui.util.ScreenUtil

/**
 * --------------------
 * | DropDownMenu(ConstraintLayout)  |
 * |  ----------------  |
 * | |      Tabs      | |
 * |  ----------------  |
 * |  ----------------  | ----------------- |
 * | |                | |                   |
 * | |                | |                   |
 * | | bottom frame   | |     top frame     |
 * | |ContentContainer| |    FrameLayout    |
 * | |                | |    menu + mask    |
 * | |                | |                   |
 * | |                | |                   |
 * |  ----------------  | ----------------- |
 * --------------------
 * Created by dongjunkun on 2015/6/17.
 *
 *
 * 原项目：https://github.com/wdeo3601/DropdownMenu
 *
 *
 * 在该项目基础上增加直接设置某个Tab标题的方法，并添加深色模式支持
 */
class DropDownMenu : ConstraintLayout {
    private val icons: Array<Drawable?> = arrayOfNulls<Drawable>(2)

    //顶部菜单布局
    private lateinit var tabMenuView: LinearLayout

    //底部容器，包含popupMenuViews，maskView
    private lateinit var containerView: FrameLayout

    //弹出菜单父布局
    private lateinit var popupMenuViews: FrameLayout

    //遮罩半透明View，点击可关闭DropDownMenu
    private var maskView: View? = null

    //tabMenuView里面选中的tab位置，-1表示未选中
    var currentTab = -1
        private set

    //tab选中颜色
    private var textSelectedColor = -0x76f37b

    //tab未选中颜色
    private var textUnselectedColor = -0xeeeeef

    //tab字体大小
    private var menuTextSize = 14

    //tab标签上下padding
    private var menuTextPadding = 0

    //图标的大小
    private var menuIconSize = 0

    //tab图标距离文字的间距
    private var drawablePadding = 0

    //tab选中图标
    private var menuSelectedIcon = 0

    //tab未选中图标
    private var menuUnselectedIcon = 0

    //内容 View 的 id
    private var contentViewId = 0

    //菜单高度百分比
    private var menuHeightPercent = 0f

    //遮罩颜色
    private var maskColor = -0x77777778
    private var onMenuStateChangeListener: OnMenuStateChangeListener? = null

    constructor(context: Context) : super(context, null) {}

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        //为DropDownMenu添加自定义属性
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu)

        //接收 tab 属性
        menuTextSize =
            a.getDimensionPixelSize(R.styleable.DropDownMenu_ddMenuTextSize, menuTextSize)
        menuTextPadding =
            a.getDimensionPixelSize(R.styleable.DropDownMenu_ddTextPadding, menuTextPadding)
        menuIconSize =
            a.getDimensionPixelSize(R.styleable.DropDownMenu_ddMenuIconSize, menuIconSize)
        drawablePadding =
            a.getDimensionPixelSize(R.styleable.DropDownMenu_ddMenuIconPadding, drawablePadding)
        textSelectedColor =
            a.getColor(R.styleable.DropDownMenu_ddTextSelectedColor, textSelectedColor)
        menuSelectedIcon =
            a.getResourceId(R.styleable.DropDownMenu_ddMenuSelectedIcon, menuSelectedIcon)
        textUnselectedColor =
            a.getColor(R.styleable.DropDownMenu_ddTextUnselectedColor, textUnselectedColor)
        menuUnselectedIcon =
            a.getResourceId(R.styleable.DropDownMenu_ddMenuUnselectedIcon, menuUnselectedIcon)

        //下划线属性
        val underlineColor: Int = a.getColor(R.styleable.DropDownMenu_ddUnderlineColor, 0)
        val underlineHeight: Int =
            a.getDimensionPixelSize(R.styleable.DropDownMenu_ddUnderlineWidth, 0)

        //内容 View 的 id
        contentViewId = a.getResourceId(R.styleable.DropDownMenu_ddFrameContentView, -1)

        //菜单属性
        menuHeightPercent =
            a.getFloat(R.styleable.DropDownMenu_ddMenuHeightPercent, menuHeightPercent)

        //蒙板属性
        maskColor = a.getColor(R.styleable.DropDownMenu_ddMaskColor, maskColor)
        a.recycle()

        //初始化tabMenuView并添加到tabMenuView
        addTabMenuViewContainer(context)

        //为tabMenuView添加下划线
        addUnderLineIfNeeded(underlineColor, underlineHeight)
    }

    /**
     * xml 加载完成
     * inflate third step: add content container
     * then, invoke fourth inflate step
     */
    protected override fun onFinishInflate() {
        super.onFinishInflate()
        val contentView: View = getViewById(contentViewId)
        val params: ConstraintLayout.LayoutParams =
            ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        params.topToBottom = R.id.dd_tab_menu_view
        params.bottomToBottom = ConstraintSet.PARENT_ID
        params.startToStart = ConstraintSet.PARENT_ID
        params.endToEnd = ConstraintSet.PARENT_ID
        contentView.layoutParams = params
        //初始化containerView并将其添加到DropDownMenu
        addPopupAndMaskContainer()
    }

    /**
     * inflate first step: add tab container
     *
     * @param context
     */
    private fun addTabMenuViewContainer(context: Context) {
        tabMenuView = LinearLayout(context)
        val params: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.topToTop = ConstraintSet.PARENT_ID
        params.startToStart = ConstraintSet.PARENT_ID
        params.endToEnd = ConstraintSet.PARENT_ID
        tabMenuView.setId(R.id.dd_tab_menu_view)
        tabMenuView.setOrientation(LinearLayout.HORIZONTAL)
        tabMenuView.setLayoutParams(params)
        addView(tabMenuView)
    }

    /**
     * inflate second step: add under line view
     *
     * @param underlineColor
     * @param underlineHeight
     */
    private fun addUnderLineIfNeeded(underlineColor: Int, underlineHeight: Int) {
        if (underlineColor != 0) {
            val underLine = View(getContext())
            val underLineParams: ConstraintLayout.LayoutParams =
                ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, underlineHeight)
            underLineParams.topToBottom = R.id.dd_tab_menu_view
            underLineParams.startToStart = ConstraintSet.PARENT_ID
            underLineParams.endToEnd = ConstraintSet.PARENT_ID
            underLine.layoutParams = underLineParams
            underLine.setBackgroundColor(underlineColor)
            addView(underLine)
        }
    }

    /**
     * inflate fourth step: add popup and mask view container
     */
    private fun addPopupAndMaskContainer() {
        containerView = FrameLayout(getContext())
        val params: ConstraintLayout.LayoutParams =
            ConstraintLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 0)
        params.startToStart = contentViewId
        params.endToEnd = contentViewId
        params.topToTop = contentViewId
        params.bottomToBottom = contentViewId
        containerView.setLayoutParams(params)
        addView(containerView)
    }

    /**
     * 初始化DropDownMenu
     */
    fun setupDropDownMenu(tabTexts: List<String>, popupViews: List<View>) {
        //设置 tab 菜单
        setUpTabMenuIfNeeded(tabTexts)
        //设置蒙板
        setUpMaskView()
        //设置弹出菜单
        setUpPopupMenuViews(popupViews)
    }

    /**
     * 设置弹出菜单
     *
     * @param popupViews
     */
    private fun setUpPopupMenuViews(popupViews: List<View>) {
        popupMenuViews = FrameLayout(getContext())
        var popupMenuHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
        if (menuHeightPercent != 0f) popupMenuHeight =
            (ScreenUtil.getScreenHeight(context) * menuHeightPercent).toInt()
        popupMenuViews.setLayoutParams(
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                popupMenuHeight
            )
        )
        popupMenuViews.setVisibility(View.GONE)
        containerView.addView(popupMenuViews)
        for (i in popupViews.indices) {
            popupViews[i].layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            popupMenuViews.addView(popupViews[i])
        }
    }

    /**
     * 设置蒙板
     */
    private fun setUpMaskView() {
        maskView = View(getContext())
        maskView!!.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        maskView!!.setBackgroundColor(maskColor)
        maskView!!.setOnClickListener { closeMenu() }
        containerView.addView(maskView)
        maskView!!.visibility = View.GONE
    }

    /**
     * 设置顶部 tab 菜单
     *
     * @param tabTexts
     */
    private fun setUpTabMenuIfNeeded(tabTexts: List<String>) {
        if (tabTexts.isEmpty()) {
            tabMenuView.setVisibility(View.GONE)
        } else {
            for (i in tabTexts.indices) {
                addTab(tabTexts, i)
            }
        }
    }

    val isSetup: Boolean
        get() = popupMenuViews != null

    private fun addTab(tabTexts: List<String>, i: Int) {
        val tab = DrawableCenterTextView(getContext())
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, menuTextSize.toFloat())
        tab.setLayoutParams(LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f))
        tab.setTextColor(textUnselectedColor)
        tab.setCompoundDrawablePadding(drawablePadding)
        initMenuIcons()
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, icons[0], null)
        tab.setText(tabTexts[i])
        tab.setPadding(0, menuTextPadding, 0, menuTextPadding)
        //添加点击事件
        tab.setOnClickListener(View.OnClickListener { switchMenu(i) })
        tabMenuView.addView(tab)
    }

    /**
     * 改变选中tab文字
     */
    fun setTabText(texts: List<String?>) {
        if (tabMenuView.getVisibility() == View.GONE) return
        if (texts.size < tabMenuView.getChildCount()) return
        for (i in 0 until tabMenuView.getChildCount()) (tabMenuView.getChildAt(i) as TextView).setText(
            texts[i]
        )
    }

    /**
     * 获取指定位置tab
     */
    fun setTabText(title: String?, index: Int) {
        (tabMenuView.getChildAt(index) as TextView).setText(title)
    }

    /**
     * 获取指定位置tab
     */
    fun setTabText(title: Int, index: Int) {
        (tabMenuView.getChildAt(index) as TextView).setText(title)
    }

    /**
     * 关闭菜单
     */
    fun closeMenu() {
        if (currentTab != -1) {
            setMenuTab(currentTab, false)
            popupMenuViews.setVisibility(View.GONE)
            popupMenuViews.setAnimation(
                AnimationUtils.loadAnimation(
                    getContext(),
                    R.anim.dd_menu_out
                )
            )
            maskView!!.visibility = View.GONE
            maskView!!.animation = AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_out)
            currentTab = -1
            if (onMenuStateChangeListener != null) onMenuStateChangeListener!!.onMenuClose()
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     */
    val isShowing: Boolean
        get() = currentTab != -1

    /**
     * 切换菜单
     */
    fun switchMenu(clickPosition: Int) {
        if (currentTab == -1) {
            popupMenuViews.setVisibility(View.VISIBLE)
            popupMenuViews.setAnimation(
                AnimationUtils.loadAnimation(
                    getContext(),
                    R.anim.dd_menu_in
                )
            )
            maskView!!.visibility = View.VISIBLE
            maskView!!.animation = AnimationUtils.loadAnimation(getContext(), R.anim.dd_mask_in)
            popupMenuViews.getChildAt(clickPosition).setVisibility(View.VISIBLE)
            for (i in 0 until popupMenuViews.getChildCount()) {
                if (i == clickPosition) popupMenuViews.getChildAt(i)
                    .setVisibility(View.VISIBLE) else popupMenuViews.getChildAt(i).setVisibility(
                    View.GONE
                )
            }
            currentTab = clickPosition
            setMenuTab(clickPosition, true)
            if (onMenuStateChangeListener != null) onMenuStateChangeListener!!.onMenuShow(currentTab)
        } else {
            if (currentTab == clickPosition) {
                closeMenu()
            } else {
                //关闭原来的菜单
                setMenuTab(currentTab, false)
                popupMenuViews.getChildAt(currentTab).setVisibility(View.GONE)

                //打开新的菜单
                popupMenuViews.getChildAt(clickPosition).setVisibility(View.VISIBLE)
                setMenuTab(clickPosition, true)
                currentTab = clickPosition
                if (onMenuStateChangeListener != null) onMenuStateChangeListener!!.onMenuShow(
                    currentTab
                )
            }
        }
    }

    private fun initMenuIcons() {
        if (icons[0] == null) {
            icons[0] = resource2VectorDrawable(menuUnselectedIcon, menuIconSize)
            icons[1] = resource2VectorDrawable(menuSelectedIcon, menuIconSize)
        }
    }

    private fun setMenuTab(i: Int, isSelected: Boolean) {
        if (tabMenuView.getVisibility() == View.GONE) return
        var p = 0
        if (isSelected) p = 1
        val tv: TextView = tabMenuView.getChildAt(i) as TextView
        if (isSelected) tv.setTextColor(textSelectedColor) else tv.setTextColor(textUnselectedColor)
        tv.setCompoundDrawablesWithIntrinsicBounds(
            null, null,
            icons[p], null
        )
    }

    /**
     * @param resourceId drawable resourceId
     * @param iconSize   pixel size
     * @return Resized bitmap
     */
    private fun resource2VectorDrawable(resourceId: Int, iconSize: Int): Drawable {
        var iconSize = iconSize
        val context: Context = getContext()
        val drawable: Drawable = AppCompatResources.getDrawable(context, resourceId)
            ?: throw Resources.NotFoundException("Resource not found : %s.$resourceId")
        if (iconSize == 0) {
            iconSize = drawable.getIntrinsicWidth()
        }
        // Resize Bitmap
        return BitmapDrawable(
            context.resources,
            Bitmap.createScaledBitmap(
                drawable2Bitmap(drawable, iconSize, iconSize),
                iconSize,
                iconSize,
                true
            )
        )
    }

    /**
     * Convert to bitmap from drawable
     */
    private fun drawable2Bitmap(drawable: Drawable, iconWidth: Int, iconHeight: Int): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(iconWidth, iconHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun setOnMenuStateChangeListener(onMenuStateChangeListener: OnMenuStateChangeListener?) {
        this.onMenuStateChangeListener = onMenuStateChangeListener
    }

    interface OnMenuStateChangeListener {
        fun onMenuShow(tabPosition: Int)
        fun onMenuClose()
    }
}