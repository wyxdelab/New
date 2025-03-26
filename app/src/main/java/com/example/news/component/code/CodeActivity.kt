package com.example.news.component.code

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.user.User
import com.example.news.config.Config
import com.example.news.databinding.ActivityCodeBinding
import com.example.news.util.Constant
import com.example.news.util.ImageUtil
import com.example.superui.extension.shortToast
import com.example.superui.util.StorageUtil
import com.example.superui.util.SuperViewUtil
import com.king.zxing.util.CodeUtils
import kotlinx.coroutines.launch

class CodeActivity : BaseTitleActivity<ActivityCodeBinding>() {
    private lateinit var viewModel: CodeViewModel

    override fun initDatum() {
        super.initDatum()
        val viewModelFactory = CodeViewModelFactory(extraString(Constant.ID))
        viewModel = ViewModelProvider(this, viewModelFactory).get(CodeViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data.collect {
                data -> showData(data)
            }
        }


        viewModel.loadData()
    }

    private fun showData(data: User) {
        //头像
        ImageUtil.showAvatar(binding.icon, data.icon, round = true)

        //昵称
        binding.nickname.setText(data.nickname)

        //我们这里的二维码的数据
        //就是一个网址
        //真实的数据在网址的查询参数里面
        //http://www.ixuea.com?u=
        val codeString: String = String.format(Config.USER_QRCODE_URL, data.id)

        //生成二维码
        showCode(codeString)
    }

    /**
     * 生成二维码
     *
     * @param data
     */
    private fun showCode(data: String) {
        //生成二维码最好放子线程生成防止阻塞UI
        //这里只是演示
//        val logo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
//
//        //生成二维码
//        //logo会覆盖到二维码
//        //暂时没找到设置方法
//        //所以就不设置了
//        val bitmap =  CodeUtils.createQRCode(data, binding.code.width,logo)
        val bitmap = CodeUtils.createQRCode(data, binding.code.width)

        //显示二维码
        binding.code.setImageBitmap(bitmap)
    }

    /**
     * 返回要显示的菜单
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
            R.id.save -> saveClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveClick() {
        //从View生成一张图片
        //Bitmap就是常说的位图
        val bitmap = SuperViewUtil.captureBitmap(binding.codeContainer)

        //保存到相册
        StorageUtil.savePicture(hostActivity, bitmap)
        R.string.success_save.shortToast()
    }
}