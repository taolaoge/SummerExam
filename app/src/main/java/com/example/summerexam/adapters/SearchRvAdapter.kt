package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.beans.FirstTextResponseItem

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/19
 */
class SearchRvAdapter(private val data: ArrayList<FirstTextResponseItem>) :
    RecyclerView.Adapter<SearchRvAdapter.TextHolder>() {
    inner class TextHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextHolder {
        return TextHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_item_first_text, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TextHolder, position: Int) {

    }

    override fun getItemCount(): Int = data.size
}