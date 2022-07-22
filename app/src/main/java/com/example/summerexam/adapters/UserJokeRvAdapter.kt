package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.extensions.gone
import com.example.summerexam.extensions.visible
import com.example.summerexam.view.PrepareView

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/22
 */
class UserJokeRvAdapter(
    private val data: ArrayList<FirstTextResponseItem>,
    private val clickLikeOrDislike: (id: Int, status: Boolean, position: Int, what: Boolean) -> Unit,
    private val clickComment: ((Int) -> Unit),
    private val clickPicture: (String) -> Unit,
    private val clickVideo: (Int) -> Unit
) :
    RecyclerView.Adapter<UserJokeRvAdapter.InnerHolder>() {
    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mImgLike: ImageView = view.findViewById(R.id.img_user_joke_like)
        val mImgDislike: ImageView = view.findViewById(R.id.img_user_joke_dislike)
        val mImgComment: ImageView = view.findViewById(R.id.img_user_joke_comment)
        val mTvContent: TextView = view.findViewById(R.id.tv_user_joke_content)
        val mImgPicture: ImageView = view.findViewById(R.id.img_picture_user_joke)
        val mCvPicture: CardView = view.findViewById(R.id.cv_picture_container_user_joke)
        val mPlayerContainer =
            itemView.findViewById<FrameLayout?>(R.id.fl_player_container_user_joke)
        val mPrepareView = itemView.findViewById<PrepareView>(R.id.prepare_view_user_joke)
        val mThumb = mPrepareView.findViewById<ImageView>(R.id.thumb)
        var mPosition = 0
        init {

            //通过tag将ViewHolder和itemView绑定
            itemView.tag = this
            mImgLike.setOnClickListener {
                data[adapterPosition].run {
                    clickLikeOrDislike(joke.jokesId, !info.isLike,adapterPosition, true)
                }
            }
            mImgDislike.setOnClickListener {
                data[adapterPosition].run {
                    clickLikeOrDislike(joke.jokesId, !info.isUnlike, adapterPosition, false)
                }
            }
            mImgComment.setOnClickListener {
                data[adapterPosition].run {
                    clickComment(joke.jokesId)
                }
            }
            mPlayerContainer.setOnClickListener {
                clickVideo(mPosition)
            }
            mImgPicture.setOnClickListener {
                clickPicture(data[adapterPosition].joke.imageUrl.decrypt())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycle_item_user_joke, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val text = data[position]
        holder.mPosition = position
        holder.run {
            mTvContent.text = text.joke.content
            if (text.joke.imageUrl.decrypt() != "") {
                holder.mCvPicture.visible()
                Glide.with(itemView.context).load(text.joke.imageUrl.decrypt())
                    .override(500, 500)
                    .centerCrop()
                    .into(this.mImgPicture)
            } else holder.mCvPicture.gone()
            if (text.joke.videoUrl.decrypt() != "") {
                holder.mPlayerContainer.visible()
                Glide.with(holder.mThumb.context)
                    .load(text.joke.thumbUrl.decrypt())
                    .into(holder.mThumb)
            } else holder.mPlayerContainer.gone()
        }
    }

    override fun getItemCount(): Int = data.size

    /**
     * 差分刷新固定写法
     */
    class DiffCallBack(
        private val mOldData: List<FirstTextResponseItem>,
        private val mNewData: List<FirstTextResponseItem>
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

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any = ""
    }
}