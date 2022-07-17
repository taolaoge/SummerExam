package com.example.summerexam.adapters

import android.text.BoringLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.summerexam.R
import com.example.summerexam.beans.OnlyTextResponseItem
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.extensions.decrypt1
import com.ndhzs.lib.common.extensions.gone
import com.ndhzs.lib.common.extensions.invisible
import com.ndhzs.lib.common.extensions.visible
import org.w3c.dom.Text

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class OnlyTextRvAdapter(
    private val data: ArrayList<OnlyTextResponseItem>,
    private val clickLikeOrDislike: (id: Int, status: Boolean, position: Int, what: Boolean) -> Unit,
    private val clickComment: ((Int) -> Unit),
    private val clickFollowing: (Boolean, String, Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_TEXT = 0
    private val TYPE_BOTTOM = 1

    inner class OnlyTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvFollowing: TextView = view.findViewById(R.id.tv_text_following)
        val mImgAvatar: ImageView = view.findViewById(R.id.img_text_avatar)
        val mTvNickname: TextView = view.findViewById(R.id.tv_text_nickname)
        val mTvSignature: TextView = view.findViewById(R.id.tv_text_signature)
        val mTvContent: TextView = view.findViewById(R.id.tv_text_content)
        val mTvLike: TextView = view.findViewById(R.id.tv_text_like_detail)
        val mTvDislike: TextView = view.findViewById(R.id.tv_text_dislike_detail)
        val mTvComment: TextView = view.findViewById(R.id.tv_text_comment_detail)
        val mTvShare: TextView = view.findViewById(R.id.tv_text_share_detail)
        val mImgLike: ImageView = view.findViewById(R.id.img_text_like)
        val mImgDislike: ImageView = view.findViewById(R.id.img_text_dislike)
        val mImgComment: ImageView = view.findViewById(R.id.img_text_comment)
        val mImgPicture: ImageView = view.findViewById(R.id.img_picture)
        //val mVideoVideo = view.findViewById(R.id.video_view_video)

        init {
            mImgLike.setOnClickListener {
                data[adapterPosition].run {
                    clickLikeOrDislike(joke.jokesId, !info.isLike, adapterPosition, true)
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
            mTvFollowing.setOnClickListener {
                data[adapterPosition].run {
                    clickFollowing(!info.isAttention, user.userId.toString(), adapterPosition)
                }
            }
        }
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

    /**
     * 获取item的类型
     */
    override fun getItemViewType(position: Int): Int {
        return if (position == data.size) 1 else 0
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
                if (data.info.isAttention) mTvFollowing.invisible() else mTvFollowing.visible()
                if (data.joke.imageUrl.decrypt() != "")
                    Glide.with(itemView.context).load(data.joke.imageUrl.decrypt())
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                        .into(this.mImgPicture)
                else holder.mImgPicture.gone()
                /*if (data.joke.videoUrl.decrypt()?.isNotEmpty() == true  ){}
                else holder.mVideoVideo.gone()*/
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