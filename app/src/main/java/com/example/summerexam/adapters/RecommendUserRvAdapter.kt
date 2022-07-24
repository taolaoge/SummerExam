package com.example.summerexam.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.beans.AttentionRecommendResponseItem
import com.example.summerexam.network.TAG

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/19
 */
class RecommendUserRvAdapter(
    private val block: (Boolean, String, Int, RecyclerView) -> Unit,
    private val rv: RecyclerView,
    private val clickAvatar:(String) ->Unit
) :
    ListAdapter<AttentionRecommendResponseItem,RecommendUserRvAdapter.UserHolder>(DiffCallBack) {

    inner class UserHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImgAvatar: ImageView = view.findViewById(R.id.img_recommend_user_avatar)
        val mTvNickname: TextView = view.findViewById(R.id.tv_recommend_user_nickname)
        val mTvJoke: TextView = view.findViewById(R.id.tv_recommend_user_joke)
        val mTvFollowers: TextView = view.findViewById(R.id.tv_recommend_user_followers)
        val mBtnFollow: Button = view.findViewById(R.id.btn_recommend_user_follow)

        init {
            mBtnFollow.setOnClickListener {
                getItem(adapterPosition).run {
                    block(!isAttention, userId.toString(), adapterPosition, rv)
                }
            }
            mImgAvatar.setOnClickListener {
                clickAvatar(getItem(adapterPosition).userId.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recyle_item_recycle_item_recommend_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = getItem(position)
        holder.run {
            Glide.with(itemView.context).load(user.avatar).into(mImgAvatar)
            mTvJoke.text = "发表 ${user.jokesNum}"
            mTvFollowers.text = "粉丝 ${user.fansNum}"
            mTvNickname.text = user.nickname
            if (user.isAttention) {
                mBtnFollow.text = "已关注"
            } else {
                mBtnFollow.text = "+关注"
            }
        }
    }


    /**
     * 差分刷新固定写法
     */
    object DiffCallBack :
        DiffUtil.ItemCallback<AttentionRecommendResponseItem>() {
        override fun areItemsTheSame(
            oldItem: AttentionRecommendResponseItem,
            newItem: AttentionRecommendResponseItem
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(
            oldItem: AttentionRecommendResponseItem,
            newItem: AttentionRecommendResponseItem
        ): Boolean {
            return oldItem == newItem
        }


    }
}