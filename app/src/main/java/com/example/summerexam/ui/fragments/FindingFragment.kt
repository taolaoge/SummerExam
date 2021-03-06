package com.example.summerexam.ui.fragments

import android.content.Intent
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
import com.example.summerexam.ui.activities.UserinfoActivity
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
            mAdapter = TiktokVpAdapter(requireContext(), ::clickComment, ::clickLike,::clickAvatar)
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_finding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initViewPager()
    }

    private fun initViewPager() {
        mVpTiktok.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mAdapter.releaseVideoView()
                if (position == viewModel.tiktokList.value?.size?.minus(1))
                    viewModel.getTiktokList()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        mAdapter.releaseVideoView()
    }

    private fun initObserver() {
        mVpTiktok.adapter = mAdapter
        viewModel.tiktokList.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
        viewModel.needRefresh.observe(viewLifecycleOwner) {
            if (it) {
                mAdapter.notifyItemChanged(viewModel.freshPosition, "")
                viewModel.changeNeedRefresh()
            }
        }
    }

    private fun clickComment(position: Int) {
        val commentBottomFragment = CommentBottomFragment()
        commentBottomFragment.arguments = Bundle().apply {
            putInt(
                "jokeId",
                viewModel.tiktokList.value?.get(position)?.joke?.jokesId ?: 1
            )
        }
        commentBottomFragment.show(
            this@FindingFragment.childFragmentManager,
            "CommentBottomFragment"
        )
    }

    private fun clickLike(position: Int) {
        viewModel.likeJoke(position)
    }

    private fun clickAvatar(position:Int){
        val intent = Intent(requireContext(),UserinfoActivity::class.java)
        intent.putExtra("userId",
        viewModel.tiktokList.value?.get(position)?.user?.userId.toString())
        startActivity(intent)
    }

}