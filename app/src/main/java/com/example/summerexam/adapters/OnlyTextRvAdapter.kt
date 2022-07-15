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
import com.example.summerexam.beans.OnlyTextResponseItem

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class OnlyTextRvAdapter(private val data: ArrayList<OnlyTextResponseItem>
,private val clickLikeOrDislike:(id:Int, status:Boolean, position:Int, what:Boolean)->Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_TEXT = 0
    private val TYPE_BOTTOM = 1

    inner class OnlyTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.findViewById<ImageView>(R.id.img_like).setOnClickListener {
                data[adapterPosition].run {
                    clickLikeOrDislike(joke.jokesId,!info.isLike,adapterPosition,true)
                }
            }
            view.findViewById<ImageView>(R.id.img_dislike).setOnClickListener {
                data[adapterPosition].run {
                    clickLikeOrDislike(joke.jokesId,!info.isUnlike,adapterPosition,false)
                }
            }
            view.findViewById<ImageView>(R.id.img_comment).setOnClickListener {

            }
        }

        val mImgAvatar: ImageView = view.findViewById(R.id.img_avatar)
        val mTvNickname: TextView = view.findViewById(R.id.tv_nickname)
        val mTvSignature: TextView = view.findViewById(R.id.tv_signature)
        val mTvContent: TextView = view.findViewById(R.id.tv_content)
        val mTvLike: TextView = view.findViewById(R.id.tv_like_detail)
        val mTvDislike: TextView = view.findViewById(R.id.tv_dislike_detail)
        val mTvComment: TextView = view.findViewById(R.id.tv_comment_detail)
        val mTvShare: TextView = view.findViewById(R.id.tv_share_detail)
        val mImgLike: ImageView = view.findViewById(R.id.img_like)
        val mImgDislike: ImageView = view.findViewById(R.id.img_dislike)
        val mImgComment: ImageView = view.findViewById(R.id.img_comment)
    }

    inner class BottomHolder(view: View) : RecyclerView.ViewHolder(view) {}


    /**
     *得到当前position的item的种类
     *有了类型的判断就可以在onCreateViewHolder中首先判断viewType的种类，对应的item的类型填充不同的视图
     *这一步是必须要判断的，如果不判断的种类的话，在onCreateViewHolder中会造成item的类型错乱，显示错乱等等
     */


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            OnlyTextViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.only_text_recycle_item, parent, false)
            )
        } else {
            BottomHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.only_text_recycle_item_bottom, parent, false)
            )
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (position == data.size) TYPE_BOTTOM else TYPE_TEXT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OnlyTextViewHolder) {
            val data = data[position]
            holder.run {
                Glide.with(this.itemView.context).load(data.user.avatar).into(this.mImgAvatar)
                this.mTvNickname.text = data.user.nickName
                this.mTvSignature.text = data.user.signature
                this.mTvContent.text = data.joke.content
                this.mTvLike.text = data.info.likeNum.toString()
                this.mTvDislike.text = data.info.disLikeNum.toString()
                this.mTvComment.text = data.info.commentNum.toString()
                this.mTvShare.text = data.info.shareNum.toString()
            }
        }
    }

    override fun getItemCount(): Int = data.size + 1

    /**
     * 差分刷新固定写法
     */
    class DiffCallBack(
        private val mOldData: List<OnlyTextResponseItem>,
        private val mNewData: List<OnlyTextResponseItem>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return mOldData.size
        }

        override fun getNewListSize(): Int {
            return mNewData.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return mOldData[oldItemPosition].joke.jokesId == mNewData[newItemPosition].joke.jokesId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return false
        }
    }
}