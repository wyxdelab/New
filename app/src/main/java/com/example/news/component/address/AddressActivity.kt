package com.example.news.component.address

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.drake.channel.sendEvent
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.addressdetail.AddressDetailActivity
import com.example.news.databinding.ActivityAddressBinding
import com.example.news.util.Constant
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * 收货地址界面
 */
class AddressActivity : BaseTitleActivity<ActivityAddressBinding>() {
    private var adapter: AddressAdapter = AddressAdapter()
    private lateinit var viewModel: AddressViewModel
    override fun initDatum() {
        super.initDatum()
//        style = extraInt(Constant.STYLE)
//        Timber.d("AddressNetworkRepository %s", repository)

        binding.list.adapter = adapter

        val viewModelFactory = AddressViewModelFactory(extraInt(Constant.STYLE))
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddressViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    adapter!!.submitList(data)
                }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_add, menu)
        return true
    }

    /**
     * 菜单点击事件
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            //添加按钮
            onAddClick()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onAddClick() {
        startActivity(AddressDetailActivity::class.java)
    }

    protected override fun initListeners() {
        super.initListeners()
        adapter.setOnItemClickListener { adapter, view, position ->
            val data = adapter.getItem(position) as Address
//            if (Constant.VALUE10 == style) {
//                finish()
//                sendEvent(AddressChangedEvent(data))
//            } else {
            startActivityExtraId(AddressDetailActivity::class.java, data.id!!)
//            }
        }
    }
}