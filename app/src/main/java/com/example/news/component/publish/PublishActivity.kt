package com.example.news.component.publish

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.drake.channel.receiveEvent
import com.drake.channel.sendEvent
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.content.ImageAdaptor
import com.example.news.component.user.SelectedFriendEvent
import com.example.news.component.user.UserActivity
import com.example.news.databinding.ActivityPublishBinding
import com.example.news.util.Constant
import com.example.news.util.RichUtil
import com.example.superui.decoration.GridDividerItemDecoration
import com.example.superui.util.DensityUtil
import com.google.common.collect.Lists
import com.ixuea.chat.config.glide.GlideEngine
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.config.SelectModeConfig
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import kotlinx.coroutines.launch
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File

class PublishActivity : BaseTitleActivity<ActivityPublishBinding>() {

    private lateinit var viewModel: PublishViewModel
    private val adapter by lazy {
        ImageAdaptor()
    }
    override fun initViews() {
        super.initViews()
        //设置布局管理器
        val layoutManager = GridLayoutManager(hostActivity, 4)
        binding.list.layoutManager = layoutManager
        val itemDecoration = GridDividerItemDecoration(
            hostActivity,
            DensityUtil.dip2px(hostActivity, 5f).toInt()
        )
        binding.list.addItemDecoration(itemDecoration)
    }

    override fun initDatum() {
        super.initDatum()
        viewModel =
            ViewModelProvider(this).get(PublishViewModel::class.java)
        initViewModel(viewModel)

        binding.list.adapter = adapter

        lifecycleScope.launch {
            viewModel.success
                .collect {
                    sendEvent(ContentChangedEvent())
                    finish()
                }
        }

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    adapter.submitList(data)
                }
        }

//        viewModel.selectPosition.observe(this) {
//            if (it is Int) {
//                //不显示位置
//                binding.position.setTitle((it as Int)!!)
//            } else {
//                binding.position.setTitle((it as PoiItem).title)
//            }
//        }
//
        viewModel.loadData()
    }
    override fun initListeners() {
        super.initListeners()
        binding.content.doAfterTextChanged {
            val r = it.toString()
            val result = getString(R.string.feed_count, r.length)
            binding.count.text = result

            if (r.endsWith(RichUtil.MENTION)) {
                //输入了@

                //跳转到选择好友界面
                UserActivity.start(hostActivity, Constant.STYLE_FRIEND_SELECT)
            }

        }

//        binding.position.setOnClickListener { startActivity(SelectLocationActivity::class.java) }
//
        adapter.setOnItemClickListener { adapter, view, position ->
            if (adapter.getItem(position) is Int) {
                selectImage()
            }
        }
//
        adapter.addOnItemChildClickListener(R.id.close) { adapter, view, position ->
            adapter.removeAt(position)
        }

        receiveEvent<SelectedFriendEvent> {
            //添加用户
            // 当然如何要实现的更好，就是添加到用户光标位置
            //如果有选中，就是替换，这里就不再实现了，因为还挺麻烦，在微聊项目课程中有实现
            binding.content.append(it.data.nickname)

            //添加结尾符号
            binding.content.append(" ")

//            highlightText()
        }
//
//        receiveEvent<SelectLocationEvent> {
//            viewModel.setLocation(it.data)
//
//        }
    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.publish, menu)
        return true
    }

    /**
     * 按钮点击了
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.publish) {
            viewModel.sendClick(
                binding.content.text.toString().trim(),
                selectedImages
            )
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 选择图片
     */
    private fun selectImage() {
        PictureSelector.create(this)
            .openGallery(SelectMimeType.ofImage())
            .setImageEngine(GlideEngine.createGlideEngine())
            .setMaxSelectNum(9) // 最大图片选择数量 int
            .setMinSelectNum(1) // 最小选择数量 int
            .setImageSpanCount(3) // 每行显示个数 int
            .setSelectionMode(SelectModeConfig.MULTIPLE) // 多选 or 单选 MULTIPLE or SINGLE
            .isPreviewImage(true) // 是否可预览图片 true or false
            .isDisplayCamera(true) // 是否显示拍照按钮 true or false
            .setCameraImageFormat(PictureMimeType.JPEG) // 拍照保存图片格式后缀,默认jpeg
            //压缩
            .setCompressEngine(object : CompressFileEngine {
                override fun onStartCompress(
                    context: Context?,
                    source: ArrayList<Uri?>?,
                    call: OnKeyValueResultCallbackListener?
                ) {
                    Luban.with(context).load(source).ignoreBy(100)
                        .setCompressListener(object : OnNewCompressListener {
                            override fun onStart() {}
                            override fun onSuccess(source: String?, compressFile: File) {
                                if (call != null) {
                                    call.onCallback(source, compressFile.absolutePath)
                                }
                            }

                            override fun onError(source: String?, e: Throwable?) {
                                if (call != null) {
                                    call.onCallback(source, null)
                                }
                            }
                        }).launch()
                }
            })
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: ArrayList<LocalMedia>) {
                    viewModel.setData(Lists.newArrayList(result))
                }

                override fun onCancel() {}
            })
    }
    /**
     * 获取选中的图片
     *
     * @return
     */
    private val selectedImages: List<LocalMedia>
        private get() {
            val data: List<Any> = adapter.items
            val datum: MutableList<LocalMedia> = ArrayList<LocalMedia>()
            for (o in data) {
                if (o is LocalMedia) {
                    datum.add(o as LocalMedia)
                }
            }
            return datum
        }
}