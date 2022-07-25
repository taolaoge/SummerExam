package com.example.summerexam.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.adapters.AttentionListRvAdapter
import com.example.summerexam.baseui.BaseActivity
import com.example.summerexam.network.TAG
import com.example.summerexam.view.MyVerticalItemDecoration
import com.example.summerexam.viewmodel.AttentionListViewModel

class AttentionListActivity : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[AttentionListViewModel::class.java] }
    private val mAdapter:AttentionListRvAdapter = AttentionListRvAdapter(){
        val intent = Intent(this,UserinfoActivity::class.java)
        intent.putExtra("userId",viewModel.attentionList.value?.get(it)?.userId.toString())
        startActivity(intent)
    }
    private val mRvList by R.id.rv_attention_list.view<RecyclerView>()
        .addInitialize {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addItemDecoration(
                MyVerticalItemDecoration(20)
            )
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attention_list)
        viewModel.userId = intent.getIntExtra("userId",0).toString()
        initObserver()
        freshRecycleView()
    }

    private fun initObserver() {
        viewModel.page.observe(this){
            Log.d(TAG, "initObserver: ${viewModel.page.value}")
            viewModel.getAttentionList()
        }
        viewModel.attentionList.observe(this){
            Log.d(TAG, "initObserver: ${it.size}")
            mAdapter.submitList(it)
        }
    }

    private fun freshRecycleView() {
        mRvList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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