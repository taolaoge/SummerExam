package com.example.summerexam.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.adapters.MineCommentRvAdapter
import com.example.summerexam.baseui.BaseFragment
import com.example.summerexam.view.MyVerticalItemDecoration
import com.example.summerexam.viewmodel.MyCommentViewModel

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/24
 */
class MyCommentFragment : BaseFragment() {
    private val viewModel by lazy { ViewModelProvider(this)[MyCommentViewModel::class.java] }
    private lateinit var mAdapter: MineCommentRvAdapter
    private val mRvComment by R.id.rv_mine_comment.view<RecyclerView>()
        .addInitialize {
            layoutManager = LinearLayoutManager(context)
            mAdapter = MineCommentRvAdapter()
            adapter = mAdapter
            addItemDecoration(
                MyVerticalItemDecoration(20)
            )
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        freshRecycleView()
    }

    private fun initObserver() {
        viewModel.page.observe(viewLifecycleOwner) {
            viewModel.getCommentList()
        }
        viewModel.commentList.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }
    }

    private fun freshRecycleView() {
        mRvComment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    //获取最后一个完全显示的ItemPosition
                    val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                    val totalItem = manager.itemCount
                    if (lastVisibleItem == (totalItem - 1) && !viewModel.isLoading) {
                        viewModel.changePage()
                    }
                }
            }
        })
    }
}