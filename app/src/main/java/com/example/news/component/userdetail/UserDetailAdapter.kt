package com.example.news.component.userdetail

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.news.R
import com.example.news.adapter.BaseFragmentPagerAdapter
import com.example.news.component.content.ContentFragment
import com.example.news.component.user.User

/**
 * 用户详界面适配器
 */
class UserDetailAdapter(
    context: Context,
    fragmentManager: FragmentManager,
    private val user: User
) :
    BaseFragmentPagerAdapter<Int>(context, fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ContentFragment.newInstance(user.id)
            else -> AboutFragment.newInstance(user)
        }
    }

    /**
     * 返回标题
     *
     * @param position
     * @return
     */
    override fun getPageTitle(position: Int): CharSequence? {
        //获取字符串id
        val resourceId = titleIds[position]

        //获取字符串
        return context.resources.getString(resourceId)
    }

    companion object {
        /**
         * 标题字符串id
         */
        private val titleIds = intArrayOf(
            R.string.content,
            R.string.about
        )
    }
}