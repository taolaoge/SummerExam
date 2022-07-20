package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.beans.SystemMessageResponseItem

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/20
 */
class SystemMessageRvAdapter(private val data:ArrayList<SystemMessageResponseItem>,
private val clickMessage:() ->Unit):
RecyclerView.Adapter<SystemMessageRvAdapter.SystemHolder>(){

    inner class SystemHolder(view:View):RecyclerView.ViewHolder(view){
        init {
            itemView.setOnClickListener {
                clickMessage()
            }
        }
        val mTvTime:TextView = view.findViewById(R.id.tv_message_time)
        val mTvContent:TextView = view.findViewById(R.id.tv_message_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemHolder {
        return SystemHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_item_message_system,parent,false)
        )
    }

    override fun onBindViewHolder(holder: SystemHolder, position: Int) {
        val message = data[position]
        holder.run {
            mTvTime.text = message.timeStr
            mTvContent.text = message.content
        }
    }

    override fun getItemCount(): Int = data.size

    /**
     * 差分刷新固定写法
     */
    class DiffCallBack(
        private val mOldData: List<SystemMessageResponseItem>,
        private val mNewData: List<SystemMessageResponseItem>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return mOldData.size
        }

        override fun getNewListSize(): Int {
            return mNewData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldData[oldItemPosition].targetId== mNewData[newItemPosition].targetId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return false
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any = ""
    }
}