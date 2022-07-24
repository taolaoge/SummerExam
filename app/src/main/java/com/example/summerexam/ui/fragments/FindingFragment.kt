package com.example.summerexam.ui.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.summerexam.R
import com.example.summerexam.adapters.FirstTextRvAdapter
import com.example.summerexam.adapters.TiktokVpAdapter
import com.example.summerexam.baseui.BaseFragment
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.utils.DkVideoPlayerUtils
import com.example.summerexam.viewmodel.FindingViewModel
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
 * date : 2022/7/14
 */
class FindingFragment : BaseFragment() {
    private val viewModel by lazy { ViewModelProvider(this)[FindingViewModel::class.java] }
    private lateinit var mAdapter: TiktokVpAdapter
    private val mVpTiktok by R.id.vp2_finding.view<ViewPager2>()
        .addInitialize {
            mAdapter = TiktokVpAdapter {
                startPlay(it)
            }
        }
    private lateinit var mVideoView: VideoView<AndroidMediaPlayer>
    lateinit var mController: GestureVideoController
    private lateinit var mErrorView: ErrorView
    private lateinit var mCompleteView: CompleteView
    private lateinit var mTitleView: TitleView

    /**
     * 当前播放的位置
     */
    private var mCurPos = -1

    /**
     * 上次播放的位置，用于页面切回来之后恢复播放
     */
    private var mLastPos = mCurPos

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return inflater.inflate(R.layout.fragment_finding, container, false)
    }

    private fun initView() {
        mVideoView = VideoView(requireContext())
        mVideoView.setOnStateChangeListener(object : VideoView.SimpleOnStateChangeListener() {
            override fun onPlayStateChanged(playState: Int) {
                super.onPlayStateChanged(playState)
                //监听VideoViewManager释放，重置状态

                if (playState == VideoView.STATE_IDLE) {
                    DkVideoPlayerUtils.removeViewFormParent(mVideoView)
                    mLastPos = mCurPos
                    mCurPos = -1
                }
            }
        })
        mController = StandardVideoController(requireContext())
        mErrorView = ErrorView(requireContext())
        mController.addControlComponent(mErrorView)
        mCompleteView = CompleteView(requireContext())
        mController.addControlComponent(mCompleteView)
        mTitleView = TitleView(requireContext())
        mController.addControlComponent(mTitleView)
        mController.addControlComponent(VodControlView(requireContext()))
        mController.addControlComponent(GestureView(requireContext()))
        mController.setEnableOrientation(true)
        mVideoView.setVideoController(mController)
    }

    private fun releaseVideoView() {
        mVideoView.release()
        if (mVideoView.isFullScreen) {
            mVideoView.stopFullScreen()
        }
        if (requireActivity().requestedOrientation !== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        mCurPos = -1
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    /**
     * 由于onPause必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onPause的逻辑
     */
    private fun pause() {
        releaseVideoView()
    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    /**
     * 由于onResume必须调用super。故增加此方法，
     * 子类将会重写此方法，改变onResume的逻辑
     */
    private fun resume() {
        if (mLastPos == -1) return
        startPlay(mLastPos)
    }

    /**
     * 开始播放
     * @param position 列表位置
     */
    private fun startPlay(newPosition: Int) {
        mVideoView.setUrl(viewModel.tiktokList.value?.get(newPosition)?.joke?.videoUrl?.decrypt())
        mVideoView.setVideoController(mController);
        mVideoView.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTiktokList()
        initObserver()
    }

    private fun initObserver() {
        mVpTiktok.adapter = mAdapter
        viewModel.tiktokList.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }
}