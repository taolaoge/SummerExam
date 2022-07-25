package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.beans.AttentionListResponseItem
import com.example.summerexam.beans.FirstTextResponseItem

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/25
 */
class AttentionListRvAdapter(private val clickAvatar:(Int)->Unit) :
    ListAdapter<AttentionListResponseItem, AttentionListRvAdapter.InnerHolder>(DiffCallBack) {
    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImgAvatar: ImageView = view.findViewById(R.id.img_attention_avatar)
        val mTvNickname: TextView = view.findViewById(R.id.tv_attention_nickname)
        val mTvSignature:TextView = view.findViewById(R.id.tv_attention_signature)
        init {
            itemView.setOnClickListener {
                clickAvatar(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recycle_item_attention, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val data = getItem(position)
        holder.apply {
            Glide.with(itemView.context)
                .load(data.avatar)
                .into(mImgAvatar)
            mTvNickname.text = data.nickname
            mTvSignature.text = data.signature
        }
    }

    /**
     * 差分刷新固定写法
     */
    object DiffCallBack : DiffUtil.ItemCallback<AttentionListResponseItem>() {

        override fun areItemsTheSame(
            oldItem: AttentionListResponseItem,
            newItem: AttentionListResponseItem
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: AttentionListResponseItem,
            newItem: AttentionListResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}