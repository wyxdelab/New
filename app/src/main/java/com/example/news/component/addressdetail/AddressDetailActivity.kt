package com.example.news.component.addressdetail

import android.content.Intent
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
import com.example.news.component.address.Address
import com.example.news.databinding.ActivityAddressDetailBinding
import com.example.news.databinding.ActivityProductDetailBinding
import com.example.news.util.Constant
import com.example.superui.citypicker.RegionSelector
import kotlinx.coroutines.launch

class AddressDetailActivity : BaseTitleActivity<ActivityAddressDetailBinding>() {
    private var id: String? = null
    private lateinit var viewModel: AddressDetailViewModel

    protected override fun initDatum() {
        super.initDatum()
        //创建ViewModel
        id = intent.getStringExtra(Constant.ID)
        val viewModelFactory = AddressDetailViewModelFactory(id)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AddressDetailViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    showData(data)
                }
        }

        viewModel.loadData()

//        initVoiceInput()
    }

    private fun showData(data: Address) {
        if (id != null) {
            binding.name.setText(data.name)
            binding.phone.setText(data.telephone)
            binding.detailAddress.setText(data.detail)
            binding.defaultAddress.setChecked(data.isDefault())
        }

        binding.area.setText(data.getReceiverArea())


    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_address_detail, menu)
        return true
    }

    /**
     * 菜单点击事件
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                viewModel.save(
                    binding.name.getText().toString().trim(),
                    binding.phone.getText().toString().trim(),
                    binding.detailAddress.getText().toString().trim(),
                    if (binding.defaultAddress.isChecked()) Constant.VALUE10 else Constant.VALUE0,
                )
                return true
            }

            R.id.delete -> {
                viewModel.delete()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun initListeners() {
        super.initListeners()
        //地区点击
        binding.area.setOnClickListener { v ->
            //这里直接用省市区选择器选择，如果还需要用定位，像发布文章那样实现就行了
            RegionSelector.getInstance(this).start(this)
        }

        //语音输入点击
//        binding.voiceInput.setOnClickListener {
//            requestRecordAudioPermission()
//        }
//
//        //选择图片点击
//        binding.selectImage.setOnClickListener {
//            //可以等选择了图片，在初始化
//            AppContext.instance.initOCR()
//            selectImage()
//        }
//
//        binding.recognition.setOnClickListener {
//            viewModel.recognitionAddress(binding.input.text.toString().trim())
//        }
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
                    viewModel.setCity(province, city, area)

                }
            }
        }
    }
}
