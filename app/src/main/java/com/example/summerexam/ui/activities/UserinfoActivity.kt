package com.example.summerexam.ui.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.adapters.UserJokeRvAdapter
import com.example.summerexam.baseui.BaseActivity
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.extensions.gone
import com.example.summerexam.extensions.toast
import com.example.summerexam.network.TAG
import com.example.summerexam.ui.fragments.CommentBottomFragment
import com.example.summerexam.utils.DkVideoPlayerUtils
import com.example.summerexam.view.MyVerticalItemDecoration
import com.example.summerexam.viewmodel.UserinfoViewModel
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.*
import xyz.doikki.videoplayer.controller.GestureVideoController
import xyz.doikki.videoplayer.player.AndroidMediaPlayer
import xyz.doikki.videoplayer.player.VideoView
import xyz.doikki.videoplayer.player.VideoViewManager

class UserinfoActivity : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[UserinfoViewModel::class.java] }
    private val mImgAvatar by R.id.img_userinfo_avatar.view<ImageView>()
    private val mTvNickname by R.id.tv_userinfo_nickname.view<TextView>()
    private val mTvJoinTime by R.id.tv_userinfo_jointime.view<TextView>()
    private val mTvSignature by R.id.tv_userinfo_signature.view<TextView>()
    private val mTvFabulous by R.id.tv_userinfo_fabulous.view<TextView>()
    private val mTvFollow by R.id.tv_userinfo_following.view<TextView>()
    private val mTvFollowers by R.id.tv_userinfo_followers.view<TextView>()
    private val mImgBackground by R.id.img_userinfo_background.view<ImageView>()
    private val mImgReturn by R.id.img_userinfo_return.view<ImageView>()
    private val mBtnFollow by R.id.btn_userinfo.view<Button>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private val mRvUserinfo by R.id.rv_userinfo.view<RecyclerView>().addInitialize {
        mLayoutManager = LinearLayoutManager(context)
        overScrollMode = View.OVER_SCROLL_NEVER
        layoutManager = mLayoutManager
        adapter = UserJokeRvAdapter(
            viewModel.newTextData,
            ::clickLikeOrDislike,
            ::clickComment,
            ::clickPicture
        ) {
            startPlay(it)
        }
        addItemDecoration(
            MyVerticalItemDecoration(20)
        )
    }
    private lateinit var mVideoView: VideoView<AndroidMediaPlayer>
    lateinit var mController: GestureVideoController
    private lateinit var mErrorView: ErrorView
    private lateinit var mCompleteView: CompleteView
    private lateinit var mTitleView: TitleView

    /**
     * ?????????????????????
     */
    private var mCurPos = -1

    /**
     * ???????????????????????????????????????????????????????????????
     */
    private var mLastPos = mCurPos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        viewModel.userId = intent.getStringExtra("userId") ?: ""
        initUi()
        viewModel.getUserJoke()
        freshRecycleView(mRvUserinfo)
        initView()
        initObserver()
        mImgReturn.setOnClickListener { finish() }
        mBtnFollow.setOnClickListener { clickFollow() }
    }

    private fun initObserver() {
        viewModel.code.observe(this) {
            if (it == 200) toast("????????????")
        }
        viewModel.targetUserinfoResponse.observe(this) {
            it.run {
                mTvFabulous.text = "$likeNum ??????"
                mTvNickname.text = nickname
                mTvJoinTime.text = "??????????????? $joinTime"
                mTvSignature.text = sigbature
                mTvFollow.text = "$attentionNum ??????"
                mTvFollowers.text = "$fansNum ??????"
                Glide.with(this@UserinfoActivity).load(avatar).into(mImgAvatar)
                Glide.with(this@UserinfoActivity).load(avatar).into(mImgBackground)
                if (attentionState == 0) {
                    mBtnFollow.text = "+??????"
                } else if (attentionState == 2) {
                    mBtnFollow.text = "?????????"
                    mBtnFollow.setBackgroundResource(R.drawable.shape_btn_follow)
                } else {
                    mBtnFollow.gone()
                }
            }
        }
        viewModel.isLoading.observe(this) {
            if (!it) freshRecycleViewData()
        }
        viewModel.followSuccess.observe(this) {
            if (it == 1) {
                viewModel.targetUserinfoResponse.value?.attentionState = 1
                mBtnFollow.text = "?????????"
                mBtnFollow.setBackgroundResource(R.drawable.shape_btn_follow)
            } else if (it == 0) {
                viewModel.targetUserinfoResponse.value?.attentionState = 0
                mBtnFollow.text = "+??????"
                mBtnFollow.setBackgroundResource(R.drawable.shape_btn)
            }
        }
        viewModel.needRefresh.observe(this){
            if (it){
                freshRecycleViewData()
                viewModel.changeNeedRefresh()
            }
        }
    }

    private fun initUi() {
        viewModel.getTargetUserinfo(viewModel.userId)
    }

    private fun clickFollow() {
        if (viewModel.targetUserinfoResponse.value?.attentionState == 0)
            viewModel.followUser("1", viewModel.userId)
        else viewModel.followUser("0", viewModel.userId)
    }

    private fun initView() {
        mVideoView = VideoView(this)
        mVideoView.setOnStateChangeListener(object : VideoView.SimpleOnStateChangeListener() {
            override fun onPlayStateChanged(playState: Int) {
                super.onPlayStateChanged(playState)
                //??????VideoViewManager?????????????????????

                if (playState == VideoView.STATE_IDLE) {
                    DkVideoPlayerUtils.removeViewFormParent(mVideoView)
                    mLastPos = mCurPos
                    mCurPos = -1
                }
            }
        })
        mController = StandardVideoController(this)
        mErrorView = ErrorView(this)
        mController.addControlComponent(mErrorView)
        mCompleteView = CompleteView(this)
        mController.addControlComponent(mCompleteView)
        mTitleView = TitleView(this)
        mController.addControlComponent(mTitleView)
        mController.addControlComponent(VodControlView(this))
        mController.addControlComponent(GestureView(this))
        mController.setEnableOrientation(true)
        mVideoView.setVideoController(mController)
    }

    private fun releaseVideoView() {
        mVideoView.release()
        if (mVideoView.isFullScreen) {
            mVideoView.stopFullScreen()
        }
        if (this.requestedOrientation !== ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        mCurPos = -1
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    /**
     * ??????onPause????????????super????????????????????????
     * ????????????????????????????????????onPause?????????
     */
    private fun pause() {
        releaseVideoView()
    }

    override fun onResume() {
        super.onResume()
        resume()
    }

    /**
     * ??????onResume????????????super????????????????????????
     * ????????????????????????????????????onResume?????????
     */
    private fun resume() {
        if (mLastPos == -1) return
        startPlay(mLastPos)
    }

    /**
     * ????????????
     * @param position ????????????
     */
    private fun startPlay(newPosition: Int) {
        if (mCurPos == newPosition) return
        if (mCurPos != -1) {
            releaseVideoView()
        }
        val itemView: View = mLayoutManager.findViewByPosition(newPosition) ?: return
        mCurPos = newPosition
        mVideoView.setUrl(viewModel.newTextData[newPosition].joke.videoUrl.decrypt())
        mVideoView.setVideoController(mController)
        val viewHolder: UserJokeRvAdapter.InnerHolder =
            itemView.tag as UserJokeRvAdapter.InnerHolder
        //?????????????????????PrepareView??????????????????????????????isDissociate???????????????true, ???????????????isDissociate?????????
        mController.addControlComponent(viewHolder.mPrepareView, true)
        DkVideoPlayerUtils.removeViewFormParent(mVideoView)
        viewHolder.mPlayerContainer.addView(mVideoView, 0)
        //???????????????VideoView?????????VideoViewManager????????????????????????????????????
        VideoViewManager.instance().add(mVideoView, "list")
        mVideoView.start()
    }

    private fun freshRecycleView(rv: RecyclerView) {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //dx??????????????? dy???????????????
                //?????????????????????,???isSliding?????????true?????????????????????false
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //manager?????????LinearLayoutManager
                val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                //newState???RecycleView????????? ????????????????????????????????????
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //?????????????????????????????????ItemPosition
                    val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                    val totalItem = manager.itemCount
                    if (lastVisibleItem == (totalItem - 1) && viewModel.isLoading.value == false) {
                        viewModel.getUserJoke()
                    }
                }
            }
        })
    }

    private fun freshRecycleViewData() {
        val diffResult = DiffUtil.calculateDiff(
            UserJokeRvAdapter.DiffCallBack(
                viewModel.oldTextData, viewModel.newTextData
            ), true
        )
        diffResult.dispatchUpdatesTo(mRvUserinfo.adapter!!)
        viewModel.oldTextData.clear()
        for (i in viewModel.newTextData) viewModel.oldTextData.add(i)
    }

    private fun clickComment(id: Int) {
        val commentBottomFragment = CommentBottomFragment()
        val bundle = Bundle()
        bundle.putInt("jokeId", id)
        commentBottomFragment.arguments = bundle
        commentBottomFragment.show(
            supportFragmentManager,
            "CommentBottomFragment"
        )
    }

    /**
     * ???????????????????????????id?????????????????????????????????????????????????????????????????????????????????
     * ??????????????????????????????rv????????????????????????????????????????????????
     * ???????????????????????????????????????????????????what??????true?????????what??????false
     */
    private fun clickLikeOrDislike(id: Int, status: Boolean, position: Int, what: Boolean) {
        viewModel.newTextData[position].info.run {
            if (!what) {
                viewModel.dislikeJoke(id, status,position)
            } else {
                viewModel.likeJoke(id, status,position)
            }
        }
    }

    private fun clickPicture(url: String) {
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("url", url)
        startActivity(intent)
    }
}