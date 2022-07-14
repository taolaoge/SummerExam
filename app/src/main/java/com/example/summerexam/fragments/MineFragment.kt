package com.example.summerexam.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.example.summerexam.R
import com.example.summerexam.activities.LoginActivity
import com.ndhzs.lib.common.ui.BaseFragment

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
class MineFragment : BaseFragment() {
    private val mRelativeLayout by R.id.mine_rl_login.view<RelativeLayout>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRelativeLayout.setOnClickListener{
            val intent = Intent(context,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}