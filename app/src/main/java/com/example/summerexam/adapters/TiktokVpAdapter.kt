package com.example.summerexam.adapters

import android.content.Context
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.beans.TiktokResponseItem
import com.example.summerexam.extensions.appContext
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.extensions.gone
import com.example.summerexam.extensions.visible
import com.example.summerexam.utils.DkVideoPlayerUtils
import com.example.summerexam.view.PrepareView
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.*
import xyz.doikki.videoplayer.controller.GestureVideoController
import xyz.doikki.videoplayer.player.AndroidMediaPlayer
import xyz.doikki.videoplayer.player.VideoView
import xyz.doikki.videoplayer.player.VideoViewManager

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/24
 */
class TiktokVpAdapter(private val context:Context)
    : ListAdapter<TiktokResponseItem, TiktokVpAdapter.InnerHolder>(DiffCallBack) {
    private lateinit var mVideoView: VideoView<AndroidMediaPlayer>
    lateinit var mController: GestureVideoController
    private lateinit var mErrorView: ErrorView
    private lateinit var mCompleteView: CompleteView
    private lateinit var mTitleView: TitleView

    /**
     * 当前播放的位置
     */
    private var mCurPos = -1

    init {
        initView()
    }

    fun releaseVideoView() {
        mVideoView.release()
        if (mVideoView.isFullScreen) {
            mVideoView.stopFullScreen()
        }
    }

    private fun initView() {
        mVideoView = VideoView(context)
        mVideoView.setOnStateChangeListener(object : VideoView.SimpleOnStateChangeListener() {
            override fun onPlayStateChanged(playState: Int) {
                super.onPlayStateChanged(playState)
                //监听VideoViewManager释放，重置状态
                if (playState == VideoView.STATE_IDLE) {
                    DkVideoPlayerUtils.removeViewFormParent(mVideoView)
                    mCurPos = -1
                }
            }
        })
        mController = StandardVideoController(context)
        mErrorView = ErrorView(context)
        mController.addControlComponent(mErrorView)
        mCompleteView = CompleteView(context)
        mController.addControlComponent(mCompleteView)
        mTitleView = TitleView(context)
        mController.addControlComponent(mTitleView)
        mController.addControlComponent(VodControlView(context))
        mController.addControlComponent(GestureView(context))
        mController.setEnableOrientation(true)
        mVideoView.setVideoController(mController)
    }

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mPlayerContainer = itemView.findViewById<FrameLayout?>(R.id.fl_tiktok_container)
        val mPrepareView = itemView.findViewById<PrepareView>(R.id.prepare_view_tiktok)
        val mThumb = mPrepareView.findViewById<ImageView>(R.id.thumb)
        var mPosition = 0
        val mTvLike:TextView = itemView.findViewById(R.id.tv_tiktok_like_num)
        val mTvComment:TextView = itemView.findViewById(R.id.tv_tiktok_comment_num)
        val mTvNickname:TextView = view.findViewById(R.id.tv_tiktok_nickname)
        val mTvContent:TextView = view.findViewById(R.id.tv_tiktok_content)

        init {
            itemView.tag = this

            mPlayerContainer.setOnClickListener {
                startPlay(mPosition,this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        return InnerHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.viewpager_item_tiktok, parent, false)
        )
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val video = getItem(position)
        holder.run {
            mTvLike.text = video.info.likeNum.toString()
            mTvComment.text = video.info.commentNum.toString()
            mTvNickname.text = "@${video.user.nickName}"
            mTvContent.text = video.joke.content
            mPosition = position
            if (video.joke.videoUrl.decrypt() != "") {
                holder.mPlayerContainer.visible()
                Glide.with(holder.mThumb.context)
                    .load(video.joke.thumbUrl.decrypt())
                    .into(holder.mThumb)
            } else holder.mPlayerContainer.gone()
            startPlay(position,holder)
        }
    }

    private fun startPlay(newPosition: Int,viewHolder: InnerHolder) {
        releaseVideoView()
        mCurPos = newPosition
        mVideoView.setUrl(getItem(newPosition)?.joke?.videoUrl?.decrypt())
        mVideoView.setVideoController(mController)
        //把列表中预置的PrepareView添加到控制器中，注意isDissociate此处只能为true, 请点进去看isDissociate的解释
        mController.addControlComponent(viewHolder.mPrepareView, true)
        DkVideoPlayerUtils.removeViewFormParent(mVideoView)
        viewHolder.mPlayerContainer.addView(mVideoView, 0)
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, "list")
        mVideoView.start()
    }


    /**
     * 差分刷新固定写法
     */
    object DiffCallBack : DiffUtil.ItemCallback<TiktokResponseItem>() {

        override fun areItemsTheSame(
            oldItem: TiktokResponseItem,
            newItem: TiktokResponseItem
        ): Boolean {
            return oldItem.joke.jokesId == newItem.joke.jokesId
        }

        override fun areContentsTheSame(
            oldItem: TiktokResponseItem,
            newItem: TiktokResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}