package com.example.summerexam.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.example.summerexam.R
import xyz.doikki.videoplayer.controller.ControlWrapper
import xyz.doikki.videoplayer.controller.IControlComponent
import xyz.doikki.videoplayer.player.VideoView
import xyz.doikki.videoplayer.player.VideoViewManager

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/17
 */
class PrepareView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr), IControlComponent {

    private lateinit var mControlWrapper: ControlWrapper
    private var mThumb: ImageView
    private var mStartPlay: ImageView
    private var mLoading: ProgressBar
    private var mNetWarning: FrameLayout

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.dkplayer_layout_prepare_view, this, true)
        mThumb = findViewById(R.id.thumb)
        mStartPlay = findViewById(R.id.start_play)
        mLoading = findViewById(R.id.loading)
        mNetWarning = findViewById(R.id.net_warning_layout)
        findViewById<View>(R.id.status_btn).setOnClickListener {
            mNetWarning.visibility = GONE
            VideoViewManager.instance().setPlayOnMobileNetwork(true)
            mControlWrapper.start()
        }
    }

    override fun attach(controlWrapper: ControlWrapper) {
        mControlWrapper = controlWrapper
    }

    override fun getView(): View {
        return this
    }

    override fun onVisibilityChanged(isVisible: Boolean, anim: Animation?) {

    }

    override fun onPlayStateChanged(playState: Int) {
        when (playState) {
            VideoView.STATE_PREPARING -> {
                bringToFront()
                visibility = VISIBLE
                mStartPlay.visibility = GONE
                mNetWarning.visibility = GONE
                mLoading.visibility = VISIBLE
            }
            VideoView.STATE_PLAYING, VideoView.STATE_PAUSED, VideoView.STATE_ERROR, VideoView.STATE_BUFFERING, VideoView.STATE_BUFFERED, VideoView.STATE_PLAYBACK_COMPLETED -> visibility =
                GONE
            VideoView.STATE_IDLE -> {
                visibility = VISIBLE
                bringToFront()
                mLoading.visibility = GONE
                mNetWarning.visibility = GONE
                mStartPlay.visibility = VISIBLE
                mThumb.visibility = VISIBLE
            }
            VideoView.STATE_START_ABORT -> {
                visibility = VISIBLE
                mNetWarning.visibility = VISIBLE
                mNetWarning.bringToFront()
            }
        }
    }

    override fun onPlayerStateChanged(playerState: Int) {

    }

    override fun setProgress(duration: Int, position: Int) {

    }

    override fun onLockStateChanged(isLocked: Boolean) {

    }

    /**
     * 设置点击此界面开始播放
     */
    fun setClickStart() {
        setOnClickListener { mControlWrapper.start() }
    }
}