package com.example.news.component.search

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseSingleItemAdapter
import com.example.news.component.content.Content
import com.example.news.databinding.HeaderSearchBinding

class SearchHeaderAdapter(private val binding: HeaderSearchBinding) :
    BaseSingleItemAdapter<Content, SearchHeaderAdapter.VH>() {

    class VH(val binding: HeaderSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Content) {

        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, data: Content?) {

    }
}