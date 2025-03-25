package com.example.news.component.cart

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.news.R
import com.example.news.activity.BaseTitleActivity
import com.example.news.component.confirmorder.ConfirmOrderActivity
import com.example.news.databinding.ActivityCartBinding
import com.example.superui.dialog.SuperDialog
import hide
import kotlinx.coroutines.launch
import show
import java.math.BigDecimal

/**
 * 购物车
 */
class CartActivity : BaseTitleActivity<ActivityCartBinding>() {
    private lateinit var editMenuItem: MenuItem
    private lateinit var adapter: CartAdapter
    private lateinit var viewModel: CartViewModel

    /**
     * 选中的购物车id
     */
    private val selectCarts = ArrayList<String>()
    protected override fun initDatum() {
        super.initDatum()
        //创建ViewModel
        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        adapter = CartAdapter()
        binding.list.adapter = adapter

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    adapter.submitList(data)
                    itemSelectChanged()
                }
        }

        //删除成功
        viewModel.deleteSuccess.observe(this) { data ->
            //从购物列表删除，如果始终希望本地的列表和服务端完全保持一致
            //那这样删除完成后，可以调用列表接口重新获取
            adapter.deleteSelect()
            selectCarts.clear()
            if (isEmpty) {
                //如果没有数据了，关闭购物车界面

                //当然也可以显示为空提示，并退出编辑模式
                finish()
            }
        }

        viewModel.loadData()
    }

    private fun itemSelectChanged() {
        selectCarts.clear()
        var it: Cart
        if (isEditModel) {
            //编辑模式，不计算价格
            var count = 0
            for (i in 0 until adapter.getItemCount()) {
                it = adapter.getItem(i)!!
                if (it.select) {
                    count++
                    selectCarts.add(it.id!!)
                }
            }
            binding.delete.setEnabled(count > 0)
        } else {
            //一定要使用BigDecimal

            //总价
            var total = BigDecimal(0)
            var productPrice: BigDecimal? = null
            var count = 0
            for (i in 0 until adapter.getItemCount()) {
                it = adapter.getItem(i)!!
                if (it.select) {
                    count++
                    selectCarts.add(it.id!!)

                    //商品单价
                    productPrice = BigDecimal(java.lang.String.valueOf(it.product.price))

                    //单价乘以数量
                    total = total.add(productPrice!!.multiply(BigDecimal(it.count)))
                }
            }
            val totalPriceDouble = total.toDouble() / 100
            binding.price.setText(getString(R.string.price, totalPriceDouble))
            val isSelectProduct = count > 0
            binding.primary.isEnabled = isSelectProduct
            if (isSelectProduct) {
                binding.primary.setText(getString(R.string.settle_account_count, count))
            } else {
                binding.primary.setText(R.string.settle_account)
            }
        }

        //结算按钮点击
        binding.primary.setOnClickListener(View.OnClickListener {
            ConfirmOrderActivity.startWithCarts(
                hostActivity,
                selectCarts
            )
        })
    }

    override fun initListeners() {
        super.initListeners()
        //item子控件点击
        adapter.addOnItemChildClickListener(R.id.select_icon) { adapter, view, position ->
            val data = adapter.getItem(position) as Cart
            //选择图标点击
            data.toggleSelect()
            itemSelectChanged()
            adapter.notifyItemChanged(position)
        }

        //选择所有点击
        binding.selectAll.setOnClickListener { v ->
            if (isEmpty) {
                return@setOnClickListener
            }
            adapter.toggleSelectAll()
            itemSelectChanged()
            binding.selectIcon.setImageResource(if (adapter.isSelectAll) R.drawable.radio_button_checked else R.drawable.radio_button)
        }

        //删除按钮点击
        binding.delete.setOnClickListener { v ->
            SuperDialog.newInstance(supportFragmentManager)
                .setTitleRes(R.string.confirm_delete)
                .setOnClickListener { data -> viewModel.deleteCarts(selectCarts) }.show()
        }

        val itemDecrementClickListener = object : BaseQuickAdapter.OnItemChildClickListener<Cart> {
            override fun onItemClick(
                adapter: BaseQuickAdapter<Cart, *>,
                view: View,
                position: Int
            ) {
                val data = adapter.getItem(position) as Cart
                if (data.count > 1) {
                    data.count = data.count - 1
                    viewModel.editCart(data)
                }
                adapter.notifyItemChanged(position)
                itemSelectChanged()
            }

        }
        adapter.addOnItemChildClickListener(R.id.decrement, itemDecrementClickListener)

        adapter.addOnItemChildClickListener(R.id.increment) { adapter, view, position ->
            val data = adapter.getItem(position) as Cart
            if (data.count < 100) {
                data.count = data.count + 1
                viewModel.editCart(data)
            }
            adapter.notifyItemChanged(position)
            itemSelectChanged()

        }
    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_text, menu)
        //查找编辑按钮
        editMenuItem = menu.findItem(R.id.edit)
        return true
    }

    /**
     * 按钮点击了
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit) {
            onEditClick()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onEditClick() {
        if (isEmpty) {
            return
        }
        if (isEditModel) {
            exitEditModel()
        } else {
            //现在是结算状态

            //进入编辑状态
            editMenuItem.setTitle(R.string.complete)
            binding.settleControlContainer.hide()
            binding.editControlContainer.show()
            adapter.cancelSelectAll()
        }
    }

    private fun exitEditModel() {
        editMenuItem.setTitle(R.string.edit)
        binding.settleControlContainer.show()
        binding.editControlContainer.hide()

        //计算一次价格
        itemSelectChanged()
    }

    /**
     * 是否是编辑模型
     *
     * @return
     */
    private val isEditModel: Boolean
        private get() = binding.editControlContainer.getVisibility() === View.VISIBLE

    private val isEmpty: Boolean
        private get() = adapter.getItemCount() === 0
}