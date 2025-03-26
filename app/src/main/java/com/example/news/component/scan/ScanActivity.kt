package com.example.news.component.scan

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.code.CodeActivity
import com.example.news.component.userdetail.UserDetailActivity
import com.example.news.component.web.WebActivity
import com.example.news.config.Config
import com.example.news.databinding.ActivityScanBinding
import com.example.news.util.PreferenceUtil
import com.example.news.util.SuperUrlUtil
import com.example.superui.extension.shortToast
import com.google.zxing.Result
import com.ixuea.chat.config.glide.GlideEngine
import com.king.zxing.CameraScan
import com.king.zxing.DefaultCameraScan
import com.king.zxing.util.CodeUtils
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import org.apache.commons.lang3.StringUtils
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File

class ScanActivity : BaseTitleActivity<ActivityScanBinding>() , CameraScan.OnScanResultCallback {
    private lateinit var cameraScan: DefaultCameraScan
    protected override fun initViews() {
        super.initViews()
        //设置沉浸式状态栏
        QMUIStatusBarHelper.translucent(this)

        //状态栏文字白色
        QMUIStatusBarHelper.setStatusBarDarkMode(this)
    }

    protected override fun initDatum() {
        super.initDatum()
        cameraScan = DefaultCameraScan(this, binding.previewView)
        cameraScan.setOnScanResultCallback(this)
            .setVibrate(true)
            .startCamera()
    }

    protected override fun initListeners() {
        super.initListeners()
        binding.code.setOnClickListener { v ->
            loginAfter {
                startActivityExtraId(
                    CodeActivity::class.java,
                    PreferenceUtil.getUserId()
                )
            }
        }
        binding.flashlight.setOnClickListener { v ->
            cameraScan.enableTorch(!cameraScan.isTorchEnabled)
            binding.flashlight.setImageResource(
                if (cameraScan.isTorchEnabled) R.drawable.flashlight_on else R.drawable.flashlight_off
            )
        }
        binding.codeImage.setOnClickListener { v ->
            PictureSelector.create(hostActivity)
                .openGallery(SelectMimeType.ofImage())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setMaxSelectNum(1) // 最大图片选择数量 int
                .setMinSelectNum(1) // 最小选择数量 int
                .setImageSpanCount(3) // 每行显示个数 int
                .setSelectionMode(SelectModeConfig.SINGLE) // 多选 or 单选 MULTIPLE or SINGLE
                .isPreviewImage(true) // 是否可预览图片 true or false
                .isDisplayCamera(true) // 是否显示拍照按钮 true or false
                .setCameraImageFormat(PictureMimeType.JPEG) // 拍照保存图片格式后缀,默认jpeg
                //压缩
                .setCompressEngine(object : CompressFileEngine {
                    override fun onStartCompress(
                        context: Context,
                        source: ArrayList<Uri>,
                        call: OnKeyValueResultCallbackListener
                    ) {
                        Luban.with(context).load<Uri>(source).ignoreBy(100)
                            .setCompressListener(object : OnNewCompressListener {
                                override fun onStart() {}
                                override fun onSuccess(source: String, compressFile: File) {
                                    if (call != null) {
                                        call.onCallback(source, compressFile.absolutePath)
                                    }
                                }

                                override fun onError(source: String, e: Throwable) {
                                    if (call != null) {
                                        call.onCallback(source, null)
                                    }
                                }
                            }).launch()
                    }
                })
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(results: ArrayList<LocalMedia>) {
                        val d = results.first()
                        val path =
                            if (StringUtils.isNotBlank(d.compressPath)) d.compressPath else d.realPath
                        val result = CodeUtils.parseQRCode(path)
                        processScanResult(result)
                    }

                    override fun onCancel() {}
                })
        }
    }

    /**
     * 处理扫描结果
     * 除了自己的二维码网址，其他的webview打开
     */
    private fun processScanResult(data: String?) {
        if (TextUtils.isEmpty(data)) {
            showHint(R.string.error_empty_qrcode)
            return
        }
        if (data!!.startsWith(Config.QRCODE_URL)) {
            //解析出网址中的查询参数
            val query = SuperUrlUtil.getQueryMap(data)

            //获取值
            //获取用户id值
            val userId = query["u"] as String?
            if (StringUtils.isNotBlank(userId)) {
                //有值
                processUserCode(userId!!)
            } else {
                //显示不支持该类型
                showHint(R.string.error_not_support_qrcode_format)
            }
        } else if (SuperUrlUtil.isUrl(data)) {
            //其他网址

            //直接打开
            WebActivity.start(hostActivity, "", data)
        } else {
            //直接内容显示到界面
            WebActivity.startWithData(hostActivity, getString(R.string.san_result), data)
        }
    }

    /**
     * 处理用户二维码
     *
     * @param data
     */
    private fun processUserCode(data: String) {
        //跳转到用户详情
        startActivityExtraId(UserDetailActivity::class.java, data)

        //关闭当前界面
        finish()
    }

    /**
     * 显示提示
     */
    private fun showHint(data: Int) {
        //先暂停
        cameraScan.stopCamera()
        data.shortToast()

        //延迟后启用扫描
        //目的是防止持续扫描不正确的二维码
        //可以根据需求调整
        binding.finderView.postDelayed({ cameraScan.startCamera() }, 800)
    }

    /**
     * 扫描结果回调
     * true:连续扫描
     * false:单次扫描
     */
    override fun onScanResultCallback(data: Result): Boolean {
        processScanResult(data.text)
        return false
    }

    override fun onScanResultFailure() {}
}