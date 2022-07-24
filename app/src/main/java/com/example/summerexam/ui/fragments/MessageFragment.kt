package com.example.summerexam.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.adapters.FirstTextRvAdapter
import com.example.summerexam.adapters.SystemMessageRvAdapter
import com.example.summerexam.baseui.BaseFragment
import com.example.summerexam.ui.activities.SystemMessageActivity
import com.example.summerexam.viewmodel.MessageViewModel

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
class MessageFragment : BaseFragment() {
    private val viewModel by lazy { ViewModelProvider(this)[MessageViewModel::class.java] }
    private val mRvMessage by R.id.rv_message.view<RecyclerView>()
        .addInitialize {
            addItemDecoration(
                DividerItemDecoration(
                    context, DividerItemDecoration.VERTICAL
                )
            )
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = LinearLayoutManager(context)
            adapter = SystemMessageRvAdapter(viewModel.newData) {
                val intent = Intent(requireActivity(), SystemMessageActivity::class.java)
                startActivity(intent)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.needRefresh.observe(viewLifecycleOwner) {
            if (it) freshRecycleViewData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
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