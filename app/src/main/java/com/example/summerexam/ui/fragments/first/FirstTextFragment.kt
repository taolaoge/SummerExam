package com.example.summerexam.ui.fragments.first

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.summerexam.R
import com.example.summerexam.adapters.FirstTextRvAdapter
import com.example.summerexam.extensions.decrypt
import com.example.summerexam.ui.fragments.CommentBottomFragment
import com.example.summerexam.utils.DkVideoPlayerUtils
import com.example.summerexam.view.MyVerticalItemDecoration
import com.example.summerexam.viewmodel.FirstTextViewModel
import com.example.summerexam.extensions.appContext
import com.example.summerexam.extensions.getSp
import com.example.summerexam.extensions.toast
import com.example.summerexam.baseui.BaseFragment
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
 * date : 2022/7/15
 */
open class FirstTextFragment : BaseFragment() {
    private lateinit var mLayoutManager: LinearLayoutManager
    private val mRvText by R.id.rv_only_text.view<RecyclerView>()
        .addInitialize {
            mLayoutManager = LinearLayoutManager(context)
            overScrollMode = View.OVER_SCROLL_NEVER
            this.run {
                layoutManager = mLayoutManager
                adapter =
                    FirstTextRvAdapter(
                        viewModel.newTextData,
                        viewModel.newRecommendUserData,
                        viewModel.oldRecommendUserData,
                        ::clickLikeOrDislike,
                        ::clickComment,
                        ::clickFollowing,
                        ::clickRecommendFollowing
                    ) {
                        startPlay(it)
                    }
                addItemDecoration(
                    MyVerticalItemDecoration(20)
                )
            }
        }
    private val viewModel by lazy { ViewModelProvider(this)[FirstTextViewModel::class.java] }
    private val mSwipeLayout by R.id.swipe_layout_only_text.view<SwipeRefreshLayout>()
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
        return inflater.inflate(R.layout.fragment_first_text, container, false)
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
        viewModel.token.value = appContext.getSp("token").getString("token", "123")
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
        if (mCurPos == newPosition) return
        if (mCurPos != -1) {
            releaseVideoView()
        }
        val itemView: View
        if (viewModel.newRecommendUserData.size != 0) {
            itemView = mLayoutManager.findViewByPosition(newPosition + 1) ?: return
            mCurPos = newPosition + 1
        } else {
            itemView = mLayoutManager.findViewByPosition(newPosition) ?: return
            mCurPos = newPosition
        }
        mVideoView.setUrl(viewModel.newTextData[newPosition].joke.videoUrl.decrypt())
        mVideoView.setVideoController(mController)
        val viewHolder: FirstTextRvAdapter.OnlyTextViewHolder =
            itemView.tag as FirstTextRvAdapter.OnlyTextViewHolder
        //把列表中预置的PrepareView添加到控制器中，注意isDissociate此处只能为true, 请点进去看isDissociate的解释
        mController.addControlComponent(viewHolder.mPrepareView, true)
        DkVideoPlayerUtils.removeViewFormParent(mVideoView)
        viewHolder.mPlayerContainer.addView(mVideoView, 0)
        //播放之前将VideoView添加到VideoViewManager以便在别的页面也能操作它
        VideoViewManager.instance().add(mVideoView, "list")
        mVideoView.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        viewModel.page.value = bundle?.getInt("page")
        viewModel.keyword.value = bundle?.getString("keyword")?:""
        viewModel.userId = bundle?.getString("userId")?:""
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (!it) freshRecycleViewData()
        }
        viewModel.isSwipeLayoutRefreshing.observe(viewLifecycleOwner) {
            if (!it) mSwipeLayout.isRefreshing = false
        }
        viewModel.page.observe(viewLifecycleOwner) {
            viewModel.getOnlyText()
        }
        viewModel.noTokenIsLoading.observe(viewLifecycleOwner) {
            if (viewModel.page.value == 0) {
                if (viewModel.token.value == "123") {
                    mRvText.adapter?.notifyDataSetChanged()
                }
            }
        }
        viewModel.token.observe(viewLifecycleOwner) {
            //当token改变时，如果在关注页面
            if (viewModel.page.value == 0) {
                if (it == "123") {
                    if (viewModel.newTextData.size != 0) viewModel.clearList(){mRvText.adapter?.notifyDataSetChanged()}
                } else {
                    freshRecycleView(mRvText)
                }
            }
        }
        initSwipeLayout()
        freshRecycleView(mRvText)
    }

    private fun initSwipeLayout() {
        mSwipeLayout.setOnRefreshListener {
            if(viewModel.page.value != 5 or 6 or 7) viewModel.clearList(){mRvText.adapter?.notifyDataSetChanged()}
            else mSwipeLayout.isRefreshing = false
        }
    }

    private fun freshRecycleView(rv:RecyclerView) {
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //dx为横向滚动 dy为竖向滚动
                //如果为竖向滚动,则isSliding属性为true，横向滚动则为false
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //manager必须为LinearLayoutManager
                val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                //newState是RecycleView的状态 如果它的状态为没有滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (viewModel.page.value == 0) {
                        //获取最后一个完全显示的ItemPosition
                        val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                        val totalItem = manager.itemCount
                        if (lastVisibleItem == (totalItem - 1) && viewModel.isLoading.value == false
                            && viewModel.token.value != "123"
                        ){
                            viewModel.getOnlyText()
                        }
                    } else {
                        //获取最后一个完全显示的ItemPosition
                        val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                        val totalItem = manager.itemCount
                        if (lastVisibleItem == (totalItem - 1) && viewModel.isLoading.value == false) {
                            viewModel.getOnlyText()
                        }
                    }
                }
            }
        })
    }

    private fun freshRecycleViewData() {
        val diffResult = DiffUtil.calculateDiff(
            FirstTextRvAdapter.DiffCallBack(
                viewModel.oldTextData, viewModel.newTextData
            ), true
        )
        diffResult.dispatchUpdatesTo(mRvText.adapter!!)
    }

    private fun clickComment(id: Int) {
        val commentBottomFragment = CommentBottomFragment()
        val bundle = Bundle()
        bundle.putInt("jokeId", id)
        commentBottomFragment.arguments = bundle
        commentBottomFragment.show(
            this@FirstTextFragment.childFragmentManager,
            "CommentBottomFragment"
        )
    }

    private fun clickFollowing(status: Boolean, userId: String, position: Int) {
        if (status) viewModel.followUser("1", userId) {
            if (it) {
                toast("关注成功")
                viewModel.newTextData[position].info.isAttention = true
                freshRecycleViewData()
            }
        }
    }

    private fun clickRecommendFollowing(
        status: Boolean,
        userId: String,
        position: Int,
        block: () -> Unit
    ) {
        if (status) viewModel.followUser("1", userId) {
            if (it) {
                toast("关注成功")
                viewModel.newRecommendUserData[position].isAttention = status
                block()
                viewModel.oldRecommendUserData[position].isAttention = status
            }
        } else {
            viewModel.followUser("0", userId) {
                if (it) {
                    toast("取关成功")
                    viewModel.newRecommendUserData[position].isAttention = status
                    block()
                    viewModel.oldRecommendUserData[position].isAttention = status
                }
            }
        }
    }

    /**
     * 第一个参数为段子的id，第二个参数为你想对段子进行什么操作，点赞，取消点赞等
     * 第三个参数为此段子在rv中的位置，方便对他进行数值的修改
     * 第四个参数为用户点赞或者点踩，点赞what就为true，点踩what就为false
     */
    private fun clickLikeOrDislike(id: Int, status: Boolean, position: Int, what: Boolean) {
        viewModel.newTextData[position].info.run {
            if (!what) {
                viewModel.dislikeJoke(id, status) {
                    if (it) {
                        if (status) {
                            disLikeNum += 1
                            isUnlike = true
                        } else {
                            disLikeNum -= 1
                            isUnlike = false
                        }
                    }
                    freshRecycleViewData()
                }
            } else {
                viewModel.likeJoke(id, status) {
                    if (it) {
                        if (status) {
                            likeNum += 1
                            isLike = true
                        } else {
                            likeNum -= 1
                            isLike = false
                        }
                    }
                    freshRecycleViewData()
                }
            }
        }
    }
}