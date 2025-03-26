package com.example.news.component.userdetail

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
import com.example.news.component.chat.ChatActivity
import com.example.news.component.profile.ProfileActivity
import com.example.news.component.user.User
import com.example.news.databinding.ActivityUserDetailBinding
import com.example.news.util.ImageUtil
import com.example.news.util.PreferenceUtil
import com.example.superui.util.SuperViewUtil.removeFromParent
import com.ixuea.courses.mymusic.component.userdetail.UserDetailViewModelFactory
import hide
import kotlinx.coroutines.launch
import show

class UserDetailActivity : BaseTitleActivity<ActivityUserDetailBinding>() {
    private lateinit var editMenuItem: MenuItem
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var adapter: UserDetailAdapter
    protected override fun initViews() {
        super.initViews()
        //启动骨架控件动画，因为使用了多个骨架控件，现在有个小问题就是动画会不一样
        //如果要解决该问题，目前能想到的就是自定义该框架，提供一个方法，支持传入多个控件
        //内部统一用一个动画，这样就没有该问题
        //提示：使用了骨架屏后，肯定要耗费一点性能，不过目前大部分手机配置也很高了
        // 也不在乎这点消耗，主要是能提供用户体系那
        // 当然例如像带屏幕的温度传感器这类设备，如果是充电或者用电池这种，那能耗更重要
        binding.userSkeleton.startShimmerAnimation()
        binding.indicatorSkeleton.startShimmerAnimation()
        binding.listSkeleton.startShimmerAnimation()
    }

    override fun initDatum() {
        super.initDatum()
        val viewModelFactory = UserDetailViewModelFactory(extraId())
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserDetailViewModel::class.java)
        initViewModel(viewModel)

        lifecycleScope.launch {
            viewModel.data
                .collect { data ->
                    showData(data)
                }
        }
    }
    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }
    private fun showData(data: User) {
        if (binding.userSkeleton.getParent() != null) {
            //停止骨架动画
            binding.userSkeleton.stopShimmerAnimation()
            binding.indicatorSkeleton.stopShimmerAnimation()
            binding.listSkeleton.stopShimmerAnimation()

            //移除骨架布局
            removeFromParent(binding.userSkeleton)
            removeFromParent(binding.indicatorSkeleton)
            removeFromParent(binding.listSkeleton)

            //显示内容容器
            binding.userContainer.show()
            binding.tab.show()
            binding.list.show()
        }

        ImageUtil.showAvatar(binding.icon, data.icon)
        binding.nickname.text = data.nickname
        val info: String = resources.getString(
            R.string.user_friend_info,
            data.followingsCount,
            data.followersCount
        )
        binding.info.text = info

        //显示关注状态
        showFollowStatus(data)

        //显示编辑用户信息按钮状态
        editMenuItem.isVisible = data.id == PreferenceUtil.getUserId()
        initUI(data)
    }

    private fun initUI(data: User) {
        adapter = UserDetailAdapter(hostActivity, supportFragmentManager, data)
        binding.list.adapter = adapter
        adapter.setDatum(mutableListOf(0, 1))

        //将TabLayout和ViewPager绑定
        binding.tab.setupWithViewPager(binding.list)
    }

    private fun showFollowStatus(data: User) {
        if (data.id!! == PreferenceUtil.getUserId()) {
            //自己

            //隐藏关注按钮，发送消息按钮
            binding.follow.hide()
            binding.sendMessage.hide()
        } else {
            //判断我是否关注了该用户
            binding.follow.show()
            if (PreferenceUtil.isLogin() && data.isFollowing()) {
                //已经关注了
                binding.follow.setText(R.string.cancel_follow)
                binding.follow.setBackgroundResource(R.drawable.shape_second_border_radius_15)
                binding.follow.setTextColor(getColor(R.color.black80))

                //显示发送消息按钮
                binding.sendMessage.show()
            } else {
                //没有关注
                binding.follow.setText(R.string.follow)
                binding.follow.setBackgroundResource(R.drawable.shape_color_primary)
                binding.follow.setTextColor(getColor(R.color.white))

                //隐藏发送消息按钮
                binding.sendMessage.hide()
            }
        }
    }

    override fun initListeners() {
        super.initListeners()
        binding.follow.setOnClickListener { loginAfter { viewModel.follow() } }
        binding.sendMessage.setOnClickListener {
            loginAfter {
                startActivityExtraId(
                    ChatActivity::class.java,
                    viewModel.id
                )
            }
        }
    }

    /**
     * 返回菜单
     *
     * @param menu
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit, menu)
        return true
    }

    /**
     * 准备显示按钮
     *
     * @param menu
     * @return
     */
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        //查找编辑按钮
        editMenuItem = menu.findItem(R.id.edit)

        //隐藏
        editMenuItem.isVisible = false
        return super.onPrepareOptionsMenu(menu)
    }

    /**
     * 菜单点击了回调
     *
     * @param item
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> startActivity(ProfileActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }
}