package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.beans.SystemMessageResponseItem
import org.w3c.dom.Text

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/20
 */
class SystemDetailMessageRvAdapter(private val data: ArrayList<SystemMessageResponseItem>) :
    RecyclerView.Adapter<SystemDetailMessageRvAdapter.MessageHolder>() {
    inner class MessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvTitle:TextView = view.findViewById(R.id.tv_message_system_detail_title)
        val mTvContent: TextView = view.findViewById(R.id.tv_message_system_detail_content)
        val mTvTime: TextView = view.findViewById(R.id.tv_message_system_detail_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        return MessageHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_item_message_system_detail, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val message = data[position]
        holder.apply {
            mTvTitle.text = message.title
            mTvContent.text = message.content
            mTvTime.text = message.timeStr
        }
    }

    override fun getItemCount(): Int = data.size
}