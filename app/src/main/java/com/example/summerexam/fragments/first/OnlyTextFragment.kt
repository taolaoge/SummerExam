package com.example.summerexam.fragments.first

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
import com.example.summerexam.adapters.OnlyTextRvAdapter
import com.example.summerexam.view.MyItemDecoration
import com.example.summerexam.viewmodel.OnlyTextViewModel
import com.ndhzs.lib.common.ui.BaseFragment

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class OnlyTextFragment : BaseFragment() {
    private val mRvText by R.id.rv_only_text.view<RecyclerView>()
        .addInitialize {
            overScrollMode = View.OVER_SCROLL_NEVER
            this.run {
                layoutManager = LinearLayoutManager(context)
                adapter = OnlyTextRvAdapter(viewModel.newTextData)
                addItemDecoration(
                    MyItemDecoration(20)
                )
            }
            freshRecycleView()
        }
    private val viewModel by lazy { ViewModelProvider(this)[OnlyTextViewModel::class.java] }
    private val mSwipeLayout by R.id.swipe_layout_only_text.view<SwipeRefreshLayout>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_only_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOnlyText()
        initSwipeLayout()
    }

    private fun initSwipeLayout() {
        mSwipeLayout.setOnRefreshListener {
            viewModel.clearList()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.isLoading.observe(this){
            if (!it) freshRecycleViewData()
        }
        viewModel.isSwipeLayoutRefreshing.observe(this){
            if (!it) mSwipeLayout.isRefreshing = false
        }
    }

    private fun freshRecycleView() {
        mRvText.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    if (lastVisibleItem == (totalItem - 1) && viewModel.isLoading.value == false){
                        viewModel.getOnlyText()
                    }
                }
            }
        })
    }

    private fun freshRecycleViewData() {
        val diffResult = DiffUtil.calculateDiff(
            OnlyTextRvAdapter.DiffCallBack(
                viewModel.oldTextData, viewModel.newTextData
            ), true
        )
        diffResult.dispatchUpdatesTo(mRvText.adapter!!)
    }
}