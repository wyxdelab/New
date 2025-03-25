package com.example.news.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 通用FragmentStateAdapter
 * 主要实现了一些通用方法
 */
abstract class BaseFragmentStateAdapter<D>(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    /**
     * 数据列表
     */
    private val datum: MutableList<D> = mutableListOf()

    /**
     * 返回数量
     *
     * @return
     */
    override fun getItemCount(): Int {
        return datum.size
    }

    /**
     * 获取当前位置数据
     *
     * @param position
     * @return
     */
    open fun getData(position: Int): D {
        return datum[position]
    }

    /**
     * 设置数据
     *
     * @param datum
     */
    fun setDatum(datum: List<D>) {
        //清除原来的数据
        this.datum.clear()

        //添加数据
        this.datum.addAll(datum)

        //通知数据改变了
        notifyDataSetChanged()
    }

    /**
     * 获取数据
     *
     * @return
     */
    fun getDatum(): List<D> {
        return datum
    }

    /**
     * 添加数据列表
     *
     * @param datum
     */
    fun addDatum(datum: List<D>?) {
        //添加数据
        this.datum.addAll(datum!!)

        //刷新数据
        notifyDataSetChanged()
    }

    /**
     * 添加数据列表
     *
     * @param datum
     */
    fun addDatum(position: Int, datum: List<D>?) {
        //添加数据
        this.datum.addAll(position, datum!!)

        //刷新数据
        notifyItemRangeInserted(position, datum.size - 1)
    }

    /**
     * 添加数据
     *
     * @param data
     */
    fun addData(data: D) {
        //添加数据
        datum.add(data)

        //刷新数据
        notifyDataSetChanged()
    }
}