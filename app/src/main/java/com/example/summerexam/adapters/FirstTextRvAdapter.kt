package com.example.summerexam.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.beans.AttentionRecommendResponseItem
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.view.MyHorizontalItemDecoration
import com.example.summerexam.view.PrepareView
import com.example.summerexam.extensions.gone
import com.example.summerexam.extensions.visible
import com.example.summerexam.network.TAG

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class FirstTextRvAdapter(
    private val iClick: IClick
) :
    ListAdapter<FirstTextResponseItem, RecyclerView.ViewHolder>(
        DiffCallBack
    ) {
    private var mAdapter: RecommendUserRvAdapter? = null
    private var newRecommendUserData: List<AttentionRecommendResponseItem>? = null

    interface IClick {
        fun clickLikeOrDislike(id: Int, status: Boolean, position: Int, what: Boolean)
        fun clickComment(id: Int)
        fun clickFollowing(isAttention: Boolean, userId: String, position: Int)
        fun clickRecommendFollow(status: Boolean, userId: String, position: Int, block: () -> Unit)
        fun clickPicture(pictureUrl: String)
        fun clickAvatar(avatarUrl: String)
        fun clickVideo(position: Int)
    }

    inner class OnlyTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvFollowing: TextView = view.findViewById(R.id.tv_text_following)
        val mImgAvatar: ImageView = view.findViewById(R.id.img_text_avatar)
        val mTvNickname: TextView = view.findViewById(R.id.tv_text_nickname)
        val mTvSignature: TextView = view.findViewById(R.id.tv_text_signature)
        val mTvContent: TextView = view.findViewById(R.id.tv_user_joke_content)
        val mTvLike: TextView = view.findViewById(R.id.tv_user_text_detail)
        val mTvDislike: TextView = view.findViewById(R.id.tv_text_dislike_detail)
        val mTvComment: TextView = view.findViewById(R.id.tv_text_comment_detail)
        val mTvShare: TextView = view.findViewById(R.id.tv_text_share_detail)
        val mImgLike: ImageView = view.findViewById(R.id.img_text_like)
        val mImgDislike: ImageView = view.findViewById(R.id.img_text_dislike)
        val mImgComment: ImageView = view.findViewById(R.id.img_text_comment)
        val mImgPicture: ImageView = view.findViewById(R.id.img_picture)
        val mCvPicture: CardView = view.findViewById(R.id.cv_picture_container)
        val mPlayerContainer = itemView.findViewById<FrameLayout?>(R.id.fl_player_container)
        val mPrepareView = itemView.findViewById<PrepareView>(R.id.prepare_view)
        val mThumb = mPrepareView.findViewById<ImageView>(R.id.thumb)
        var mPosition = 0

        init {
            //通过tag将ViewHolder和itemView绑定
            itemView.tag = this

            mImgLike.setOnClickListener {
                getItem(mPosition).run {
                    iClick.clickLikeOrDislike(joke.jokesId, !info.isLike, mPosition, true)
                }
            }
            mImgDislike.setOnClickListener {
                getItem(mPosition).run {
                    iClick.clickLikeOrDislike(joke.jokesId, !info.isUnlike, mPosition, false)
                }
            }
            mImgComment.setOnClickListener {
                getItem(mPosition).run {
                    iClick.clickComment(joke.jokesId)
                }
            }
            mTvFollowing.setOnClickListener {
                getItem(mPosition).run {
                    iClick.clickFollowing(!info.isAttention, user.userId.toString(), mPosition)
                }
            }
            mPlayerContainer.setOnClickListener {
                iClick.clickVideo(mPosition)
            }
            mImgPicture.setOnClickListener {
                iClick.clickPicture(getItem(mPosition).joke.imageUrl.decrypt())
            }
            mImgAvatar.setOnClickListener {
                iClick.clickAvatar(getItem(mPosition).user.userId.toString())
            }
        }
    }

    inner class BottomHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvBottom = view.findViewById<TextView>(R.id.tv_comment)
    }

    inner class RecommendUserHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mRvRecommendUser: RecyclerView = view.findViewById(R.id.rv_recommend_user)

        init {
            mRvRecommendUser.run {
                mAdapter = RecommendUserRvAdapter(::callback, this) {
                    iClick.clickAvatar(it)
                }
                val linearLayoutManager = LinearLayoutManager(itemView.context)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = linearLayoutManager
                adapter = mAdapter
                addItemDecoration(
                    MyHorizontalItemDecoration(20)
                )
            }
            setIsRecyclable(false)
        }
    }

    private fun callback(status: Boolean, id: String, position: Int, rv: RecyclerView) {
        iClick.clickRecommendFollow(status, id, position) {
            freshRecycleViewData()
        }
    }

    /**
     *得到当前position的item的种类
     *有了类型的判断就可以在onCreateViewHolder中首先判断viewType的种类，对应的item的类型填充不同的视图
     *这一步是必须要判断的，如果不判断的种类的话，在onCreateViewHolder中会造成item的类型错乱，显示错乱等等
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                OnlyTextViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycle_item_first_text, parent, false)
                )
            }
            1 -> {
                BottomHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycle_item_first_text_bottom, parent, false)
                )
            }
            else -> {
                RecommendUserHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.recycle_item_recommend_user, parent, false)
                )
            }
        }
    }

    /**
     * 获取item的类型
     */
    override fun getItemViewType(position: Int): Int {
        return if (newRecommendUserData == null){
            if (position == itemCount -1) 1
            else 0
        }
        else {
            if (position == 0) 3
            else if (position == itemCount-1) 1
            else 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OnlyTextViewHolder) {
            val text: FirstTextResponseItem
            if (newRecommendUserData == null) {
                text = getItem(position)
                holder.mPosition = position
            } else {
                text = getItem(position - 1)
                holder.mPosition = position - 1
            }
            holder.run {
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
                Glide.with(this.itemView.context).load(text.user.avatar).into(this.mImgAvatar)
                this.mTvNickname.text = text.user.nickName
                this.mTvSignature.text = text.user.signature
                this.mTvContent.text = text.joke.content
                this.mTvLike.text = text.info.likeNum.toString()
                this.mTvDislike.text = text.info.disLikeNum.toString()
                this.mTvComment.text = text.info.commentNum.toString()
                this.mTvShare.text = text.info.shareNum.toString()
                if (text.info.isAttention) mTvFollowing.gone() else mTvFollowing.visible()
            }
        }
        if (holder is RecommendUserHolder) {
            freshRecycleViewData()
        }
    }

    fun freshList(list: List<AttentionRecommendResponseItem>?) {
        newRecommendUserData = list
        freshRecycleViewData()
    }

    fun freshAttentionList(position:Int){
        mAdapter?.notifyItemChanged(position)
    }

    private fun freshRecycleViewData() {
        mAdapter?.submitList(newRecommendUserData)
    }

    /**
     * 差分刷新固定写法
     */
    object DiffCallBack : DiffUtil.ItemCallback<FirstTextResponseItem>() {
        override fun areItemsTheSame(
            oldItem: FirstTextResponseItem,
            newItem: FirstTextResponseItem
        ): Boolean {
            return oldItem.joke.jokesId == newItem.joke.jokesId
        }

        override fun areContentsTheSame(
            oldItem: FirstTextResponseItem,
            newItem: FirstTextResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

}