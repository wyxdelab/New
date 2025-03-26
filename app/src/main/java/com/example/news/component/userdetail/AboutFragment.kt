package com.example.news.component.userdetail

import android.os.Bundle
import com.example.news.R
import com.example.news.component.user.User
import com.example.news.databinding.FragmentAboutBinding
import com.example.news.fragment.BaseViewModelFragment
import com.example.news.util.Constant
import org.apache.commons.lang3.StringUtils

/**
 * 用户详情-关于界面
 */
class AboutFragment : BaseViewModelFragment<FragmentAboutBinding>() {
    override fun initDatum() {
        super.initDatum()
        val data = requireArguments().getParcelable<User>(Constant.DATA)!!
        show(data)
    }

    private fun show(data: User) {
        //昵称
        binding.nickname.text = resources
            .getString(R.string.nickname_value, data.nickname)

        //性别
        binding.gender.text = resources
            .getString(R.string.gender_value, data.getGenderFormat())

        //生日
        binding.birthday.text = resources
            .getString(R.string.birthday_value, data.birthdayFormat())

        //地区
        var area = ""
        if (StringUtils.isNotEmpty(data.province)) {
            //有省市区
            val sb = StringBuilder()

            //拼接地区
            sb.append(data.province)
            sb.append("-")
            sb.append(data.city)
            sb.append("-")
            sb.append(data.area)
            area = sb.toString()
        }
        binding.area.text = resources.getString(R.string.area_value, area)

        //描述
        binding.description.text = resources
            .getString(R.string.description_value, data.getDetailFormat())
    }

    companion object {
        fun newInstance(data: User): AboutFragment {
            val args = Bundle()
            args.putParcelable(Constant.DATA, data)

            val fragment = AboutFragment()
            fragment.arguments = args
            return fragment
        }
    }
}