package com.example.summerexam.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.adapters.SystemDetailMessageRvAdapter
import com.example.summerexam.adapters.SystemMessageRvAdapter
import com.example.summerexam.baseui.BaseActivity
import com.example.summerexam.viewmodel.MessageViewModel

class SystemMessageActivity : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[MessageViewModel::class.java] }

    private val mRvMessage by R.id.rv_message_system_detail.view<RecyclerView>()
        .addInitialize {
            addItemDecoration( DividerItemDecoration(
                context, DividerItemDecoration.VERTICAL)
            )
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context)
            adapter = SystemDetailMessageRvAdapter(viewModel.newData)
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_message)
        viewModel.needRefresh.observe(this){
            if (it) freshRecycleViewData()
        }
    }

    private fun freshRecycleViewData() {
        val diffResult = DiffUtil.calculateDiff(
            SystemMessageRvAdapter.DiffCallBack(
                viewModel.oldData, viewModel.newData
            ), true
        )
        diffResult.dispatchUpdatesTo(mRvMessage.adapter!!)
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.newData.size == 0)
            viewModel.getSystemMessage()
    }
}