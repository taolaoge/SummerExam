package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.beans.MyCommentResponseItem

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/24
 */
class MineCommentRvAdapter :
    ListAdapter<MyCommentResponseItem, MineCommentRvAdapter.InnerHolder>(DiffCallBack) {

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvNickname: TextView = view.findViewById(R.id.tv_mine_comment_who)
        val mTvComment: TextView = view.findViewById(R.id.tv_mine_comment_detail)
        val mTvContent: TextView = view.findViewById(R.id.tv_mine_comment_content)
        val mTvTime: TextView = view.findViewById(R.id.tv_mine_comment_time)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_item_mine_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val data = getItem(position)
        holder.run {
            mTvComment.text = data.extraContent
            mTvNickname.text = data.msgItemTypeDesc
            mTvTime.text = data.msgTime
            mTvContent.text = data.content
        }
    }

    /**
     * 差分刷新固定写法
     */
    object DiffCallBack : DiffUtil.ItemCallback<MyCommentResponseItem>() {

        override fun areItemsTheSame(
            oldItem: MyCommentResponseItem,
            newItem: MyCommentResponseItem
        ): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(
            oldItem: MyCommentResponseItem,
            newItem: MyCommentResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}