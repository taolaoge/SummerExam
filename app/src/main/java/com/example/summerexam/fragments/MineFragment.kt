package com.example.summerexam.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.summerexam.R
import com.example.summerexam.activities.LoginActivity
import com.example.summerexam.viewmodel.MineViewModel
import com.ndhzs.lib.common.ui.BaseFragment

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
class MineFragment : BaseFragment() {
    private val mRelativeLayout by R.id.mine_rl_login.view<RelativeLayout>()
    private val viewModel by lazy { ViewModelProvider(this)[MineViewModel::class.java] }
    private val mTvFollow by R.id.mine_tv_follow_detail.view<TextView>()
    private val mTvFollowers by R.id.mine_tv_followers_detail.view<TextView>()
    private val mTvCoin by R.id.mine_tv_coins_detail.view<TextView>()
    private val mTvUserName by R.id.mine_tv_username.view<TextView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.token.observe(this) {
            if (it != "123") {
                refreshUi()
            }
        }
    }

    private fun refreshUi() {
        viewModel.refreshUi(){
            mTvCoin.text = viewModel.coin.toString()
            mTvFollow.text = viewModel.follow.toString()
            mTvFollowers.text = viewModel.followers.toString()
            mTvUserName.text = viewModel.username
            //当请求成功并且token未过期的时候，变为false
            viewModel.isNeedRefresh = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRelativeLayout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //切换回前台后，刷新token，主要目的为登陆成功后，刷新用户信息
        if (viewModel.isNeedRefresh) viewModel.refreshToken()
    }

}