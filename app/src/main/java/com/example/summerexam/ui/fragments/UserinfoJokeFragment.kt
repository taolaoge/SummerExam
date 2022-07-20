package com.example.summerexam.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.baseui.BaseFragment
import com.example.summerexam.viewmodel.UserJokeViewModel

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/20
 */
class UserinfoJokeFragment : BaseFragment() {
    private val viewModel by lazy {ViewModelProvider(this)[UserJokeViewModel::class.java] }
    private val mRvUserinfo by R.id.rv_userinfo.view<RecyclerView>()
        .addInitialize {

        }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_userinfo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        viewModel.position.value = bundle?.getInt("position")
        viewModel.userId = bundle?.getString("userId")?:""
        viewModel.position.observe(viewLifecycleOwner){
            viewModel.getList()
        }
    }
}
