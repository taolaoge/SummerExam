package com.example.summerexam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.summerexam.R
import com.example.summerexam.beans.AttentionRecommendResponseItem
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.view.MyHorizontalItemDecoration
import com.example.summerexam.view.PrepareView
import com.example.summerexam.extensions.gone
import com.example.summerexam.extensions.visible

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class FirstTextRvAdapter(
    private val data: ArrayList<FirstTextResponseItem>,
    private val newRecommendUserData:ArrayList<AttentionRecommendResponseItem>,
    private val oldRecommendUserData:ArrayList<AttentionRecommendResponseItem>,
    private val clickLikeOrDislike: (id: Int, status: Boolean, position: Int, what: Boolean) -> Unit,
    private val clickComment: ((Int) -> Unit),
    private val clickFollowing: (Boolean, String, Int) -> Unit,
    private val clickRecommendFollow:(Boolean,String,Int,block:()->Unit) -> Unit,
    private val clickVideo:(Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        val mPlayerContainer = itemView.findViewById<FrameLayout?>(R.id.fl_player_container)
        val mPrepareView = itemView.findViewById<PrepareView>(R.id.prepare_view)
        val mThumb = mPrepareView.findViewById<ImageView>(R.id.thumb)
        var mPosition = 0

        //通过tag将ViewHolder和itemView绑定
        //通过tag将ViewHolder和itemView绑定

        init {
            //通过tag将ViewHolder和itemView绑定
            itemView.tag = this

            mImgLike.setOnClickListener {
                data[mPosition].run {
                    clickLikeOrDislike(joke.jokesId, !info.isLike,mPosition, true)
                }
            }
            mImgDislike.setOnClickListener {
                data[mPosition].run {
                    clickLikeOrDislike(joke.jokesId, !info.isUnlike, mPosition, false)
                }
            }
            mImgComment.setOnClickListener {
                data[mPosition].run {
                    clickComment(joke.jokesId)
                }
            }
            mTvFollowing.setOnClickListener {
                data[mPosition].run {
                    clickFollowing(!info.isAttention, user.userId.toString(), mPosition)
                }
            }
            mPlayerContainer.setOnClickListener {
                clickVideo(mPosition)
            }
        }
    }

    inner class BottomHolder(view: View) : RecyclerView.ViewHolder(view) {}

    inner class RecommendUserHolder(view:View):RecyclerView.ViewHolder(view){
        val mRvRecommendUser:RecyclerView = view.findViewById(R.id.rv_recommend_user)
        init {
            mRvRecommendUser.run {
                adapter = RecommendUserRvAdapter(newRecommendUserData,::callback,this)
                val linearLayoutManager = LinearLayoutManager(itemView.context)
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = linearLayoutManager
                addItemDecoration(
                    MyHorizontalItemDecoration(20)
                )
            }
        }
    }

    private fun callback(status: Boolean,id:String,position: Int,rv:RecyclerView){
        clickRecommendFollow(status,id,position){
            freshRecycleViewData(rv)
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
                        .inflate(R.layout.recycle_item_recommend_user,parent,false)
                )
            }
        }
    }

    /**
     * 获取item的类型
     */
    override fun getItemViewType(position: Int): Int {
        return if (newRecommendUserData.size == 0){
            if (position == data.size) 1 else 0
        }else{
            when (position) {
                0 -> 2
                data.size +1 -> 1
                else -> 0
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OnlyTextViewHolder) {
            val text: FirstTextResponseItem
            if (newRecommendUserData.size == 0){
                text = data[position]
                holder.mPosition = position
            }
            else{
                text = data[position-1]
                holder.mPosition = position-1
            }

            holder.run {
                if (text.joke.imageUrl.decrypt() != "") {
                    holder.mImgPicture.visible()
                    Glide.with(itemView.context).load(text.joke.imageUrl.decrypt())
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(15)))
                        .into(this.mImgPicture)
                } else holder.mImgPicture.gone()
                if (text.joke.videoUrl.decrypt() != ""){
                    holder.mPlayerContainer.visible()
                    Glide.with(holder.mThumb.context)
                        .load(text.joke.thumbUrl.decrypt())
                        .placeholder(android.R.color.darker_gray)
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
        if (holder is RecommendUserHolder){
            freshRecycleViewData(holder.mRvRecommendUser)
        }
    }

    private fun freshRecycleViewData(rv:RecyclerView) {
        val diffResult = DiffUtil.calculateDiff(
            RecommendUserRvAdapter.DiffCallBack(
                oldRecommendUserData, newRecommendUserData
            ), true
        )
        diffResult.dispatchUpdatesTo(rv.adapter!!)
    }

    override fun getItemCount(): Int {
        return if (newRecommendUserData.size == 0) data.size + 1
        else data.size+2
    }

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