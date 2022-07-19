package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.beans.AttentionRecommendResponseItem

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/19
 */
class RecommendUserRvAdapter(
    private val newData: ArrayList<AttentionRecommendResponseItem>,
    private val block: (Boolean, String, Int, RecyclerView) -> Unit,
    private val rv: RecyclerView
) :
    RecyclerView.Adapter<RecommendUserRvAdapter.UserHolder>() {

    inner class UserHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImgAvatar: ImageView = view.findViewById(R.id.img_recommend_user_avatar)
        val mTvNickname: TextView = view.findViewById(R.id.tv_recommend_user_nickname)
        val mTvJoke: TextView = view.findViewById(R.id.tv_recommend_user_joke)
        val mTvFollowers: TextView = view.findViewById(R.id.tv_recommend_user_followers)
        val mBtnFollow: Button = view.findViewById(R.id.btn_recommend_user_follow)

        init {
            mBtnFollow.setOnClickListener {
                newData[adapterPosition].run {
                    block(!isAttention, userId.toString(), adapterPosition, rv)
                }
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
        val user = newData[position]
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

    override fun getItemCount(): Int = newData.size

    /**
     * 差分刷新固定写法
     */
    class DiffCallBack(
        private val mOldData: List<AttentionRecommendResponseItem>,
        private val mNewData: List<AttentionRecommendResponseItem>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return mOldData.size
        }

        override fun getNewListSize(): Int {
            return mNewData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldData[oldItemPosition].isAttention == mNewData[newItemPosition].isAttention
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return false
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any = ""
    }
}