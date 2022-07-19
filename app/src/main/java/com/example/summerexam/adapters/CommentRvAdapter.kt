package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.beans.Comment

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/16
 */
class CommentRvAdapter(private val data: ArrayList<Comment>
,private val clickLike:(String, Boolean,Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.findViewById<TextView>(R.id.tv_comment_reply).setOnClickListener {

            }
            view.findViewById<ImageView>(R.id.img_comment_love).setOnClickListener {
                data[adapterPosition].run {
                    clickLike(commentId.toString(),!isLike,adapterPosition)
                }
            }
        }
        val mTvNickname: TextView = view.findViewById(R.id.rv_comment_nickname)
        val mTvContent:TextView = view.findViewById(R.id.tv_comment_content)
        val mTvTime:TextView = view.findViewById(R.id.tv_comment_time)
        val mTvLove:TextView = view.findViewById(R.id.tv_comment_love_detail)
        val mImgAvatar:ImageView = view.findViewById(R.id.img_comment_avatar)
    }

    inner class NoCommentViewHolder(view:View):RecyclerView.ViewHolder(view){

    }

    override fun getItemViewType(position: Int): Int {
        return if (data.size == 0) 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            CommentViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_item_comment, parent, false)
            )
        }else{
            NoCommentViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recycle_item_no_comment,parent,false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommentViewHolder) {
            val comment = data[position]
            holder.run {
                mTvNickname.text = comment.commentUser.nickname
                Glide.with(itemView.context).load(comment.commentUser.userAvatar).into(mImgAvatar)
                mTvContent.text = comment.content
                mTvLove.text = comment.likeNum.toString()
                mTvTime.text = comment.timeStr
            }
        }
    }

    override fun getItemCount(): Int{
        return if (data.size == 0) 1 else data.size
    }


    /**
     * 差分刷新固定写法
     */
    class DiffCallBack(
        private val mOldData: List<Comment>,
        private val mNewData: List<Comment>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return mOldData.size
        }

        override fun getNewListSize(): Int {
            return mNewData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldData[oldItemPosition].isLike == mNewData[newItemPosition].isLike
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return false
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any = ""
    }
}