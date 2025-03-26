package com.example.news.component.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.user.User
import com.example.news.databinding.ActivityProfileBinding
import com.example.news.util.ImageUtil
import com.example.superui.citypicker.RegionSelector
import com.example.superui.util.SuperDateUtil
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.ixuea.chat.config.glide.GlideEngine
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.engine.CropFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropImageEngine
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File

class ProfileActivity : BaseTitleActivity<ActivityProfileBinding>() {
    private lateinit var viewModel: ProfileViewModel

    protected override fun initDatum() {
        super.initDatum()
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    showData(data)
                }
        }

        viewModel.loadData()
    }

    private fun showData(data: User) {
        //头像
        ImageUtil.showAvatar(binding.icon, data.icon)

        //昵称
        binding.nickname.setText(data.nickname)

        //性别
        binding.gender.setText(data.getGenderFormat())

        //生日
        if (StringUtils.isNotBlank(data.birthday)) {
            binding.birthday.setText(data.birthdayFormat())
        }

        //地区
        if (StringUtils.isNotBlank(data.province)) {
            val area: String = resources.getString(
                R.string.area_value2,
                data.province,
                data.city,
                data.area
            )
            binding.area.text = area
        }

        //描述
        if (StringUtils.isNotBlank(data.detail)) {
            binding.detail.setText(data.getDetailFormat())
        }

        //手机号
        binding.phone.setText(data.phone)

        //邮箱
        binding.email.setText(data.email)
    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return true
    }

    /**
     * 菜单点击了
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> viewModel.save(
                binding.nickname.text.toString().trim(),
                binding.detail.text.toString().trim()
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initListeners() {
        super.initListeners()
//头像容器点击
        binding.iconContainer.setOnClickListener { selectAvatar() }

        //性别点击
        binding.genderContainer.setOnClickListener { v ->
            GenderDialogFragment
                .show(supportFragmentManager, viewModel.param.gender / 10) { dialog, which ->
                    //关闭对话框
                    dialog.dismiss()
                    when (which) {
                        1 ->                             //男
                            viewModel.setGender(User.MALE)

                        2 ->                             //女
                            viewModel.setGender(User.FEMALE)

                        else ->                             //保密
                            viewModel.setGender(User.GENDER_UNKNOWN)
                    }
                }
        }

        //生日点击
        binding.birthdayContainer.setOnClickListener { v ->
            //当前日期前可以选择，包括今天
            val before =
                DateValidatorPointBackward.now()

            //日期约束
            val calendarConstraints =
                CalendarConstraints.Builder()
                    .setValidator(
                        CompositeDateValidator.allOf(
                            listOf<CalendarConstraints.DateValidator>(
                                before,
                            )
                        )
                    )
                    .build()
            //创建选择器
            val fragment =
                MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(calendarConstraints)
                    .build()

            //确定点击
            fragment.addOnPositiveButtonClickListener { selection ->
                viewModel.setBirthday(SuperDateUtil.yyyyMMdd(selection))
            }
            fragment.show(supportFragmentManager, "MaterialDatePicker")
        }

        //地区点击
        binding.areaContainer.setOnClickListener {
            RegionSelector.getInstance(this).start(this)
        }
    }

    /**
     * 界面结果回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            //请求成功了
            when (requestCode) {
                RegionSelector.REQUEST_REGION -> {
                    //城市选择

                    //省
                    val province = RegionSelector.getProvince(data)

                    //市
                    val city = RegionSelector.getCity(data)

                    //区
                    val area = RegionSelector.getArea(data)

                    viewModel.setArea(province, city, area)

                }
            }
        }
    }

    private fun selectAvatar() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setMaxSelectNum(1) // 最大图片选择数量 int
            .setMinSelectNum(1) // 最小选择数量 int
            .setImageSpanCount(3) // 每行显示个数 int
            .setSelectionMode(SelectModeConfig.SINGLE) // 多选 or 单选 MULTIPLE or SINGLE
            .isPreviewImage(true) // 是否可预览图片 true or false
            .isDisplayCamera(true) // 是否显示拍照按钮 true or false
            .setCameraImageFormat(PictureMimeType.JPEG) // 拍照保存图片格式后缀,默认jpeg
            .setCropEngine(CropFileEngine { fragment, srcUri, destinationUri, dataSource, requestCode -> // 注意* 如果你实现自己的裁剪库，需要在Activity的.setResult();
                // Intent中需要给MediaStore.EXTRA_OUTPUT，塞入裁剪后的路径；如果有额外数据也可以通过CustomIntentKey.EXTRA_CUSTOM_EXTRA_DATA字段存入；
                val uCrop = UCrop.of(srcUri, destinationUri, dataSource)
                uCrop.setImageEngine(object : UCropImageEngine {
                    override fun loadImage(context: Context, url: String, imageView: ImageView) {
                        Glide.with(context).load(url).into(imageView)
                    }

                    override fun loadImage(
                        context: Context,
                        url: Uri,
                        maxWidth: Int,
                        maxHeight: Int,
                        call: UCropImageEngine.OnCallbackListener<Bitmap>
                    ) {
                        Glide.with(context).asBitmap().override(maxWidth, maxHeight).load(url)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    if (call != null) {
                                        call.onCall(resource)
                                    }
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    if (call != null) {
                                        call.onCall(null)
                                    }
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })
                    }
                })
                uCrop.withOptions(buildOptions())
                uCrop.start(hostActivity, fragment, requestCode)
            }) //压缩
            .setCompressEngine(CompressFileEngine { context, source, call ->
                Luban.with(context).load(source).ignoreBy(100)
                    .setCompressListener(object : OnNewCompressListener {
                        override fun onStart() {}
                        override fun onSuccess(source: String, compressFile: File) {
                            call?.onCallback(source, compressFile.absolutePath)
                        }

                        override fun onError(source: String, e: Throwable) {
                            call?.onCallback(source, null)
                        }
                    }).launch()
            })
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    viewModel.upload(result[0].cutPath)
                }

                override fun onCancel() {}
            })
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    private fun buildOptions(): UCrop.Options {
        val options = UCrop.Options()
        options.setHideBottomControls(false)
        options.setFreeStyleCropEnabled(false)
        options.setShowCropFrame(true)
        options.setShowCropGrid(true)
        options.withAspectRatio(500f, 500f)
        options.isCropDragSmoothToCenter(false)
        options.setMaxScaleMultiplier(500f)
        return options
    }

//    override fun pageId(): String? {
//        return "Profile"
//    }
}