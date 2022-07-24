package com.example.summerexam.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.ui.activities.LoginActivity
import com.example.summerexam.viewmodel.MineViewModel
import com.example.summerexam.extensions.appContext
import com.example.summerexam.extensions.edit
import com.example.summerexam.extensions.getSp
import com.example.summerexam.baseui.BaseFragment
import com.example.summerexam.network.TAG
import com.example.summerexam.ui.activities.UserinfoActivity

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
class MineFragment : BaseFragment() {
    private val mRelativeLayout by R.id.mine_cl_login.view<ConstraintLayout>()
    private val viewModel by lazy { ViewModelProvider(this)[MineViewModel::class.java] }
    private val mTvFollow by R.id.tv_mine_follow_detail.view<TextView>()
    private val mTvFollowers by R.id.tv_mine_followers_detail.view<TextView>()
    private val mTvCoin by R.id.tv_mine_coins_detail.view<TextView>()
    private val mTvUserName by R.id.tv_mine_username.view<TextView>()
    private val mBtnOut by R.id.btn_mine_out.view<Button>()
    private val mImgUser by R.id.img_mine_avatar.view<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun refreshUi() {
        viewModel.refreshUi()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        mRelativeLayout.setOnClickListener {
            if (viewModel.token.value == "123") {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(context,UserinfoActivity::class.java)
                intent.putExtra("userId",viewModel.userInfoResponse.value?.user?.userId.toString())
                Log.d(TAG, "onViewCreate: ${viewModel.userInfoResponse.value?.user?.userId}")
                startActivity(intent)
            }
        }
        mBtnOut.setOnClickListener {
            appContext.getSp("token").edit {
                clear()
            }
            //退出登陆就是把token清除
            viewModel.refreshToken()
            //token被clear了，所以对token的状态为需要token
            viewModel.isNeedToken = true
            //初始化ui，将ui变为初始的样子
            initUi()
        }
        Glide.with(this).load("https://jokes-avatar.oss-cn-beijing.aliyuncs.com/aliyun/jokes/avatar/default_avatar.png")
            .into(mImgUser)
    }

    private fun initObserver() {
        viewModel.token.observe(viewLifecycleOwner) {
            //观察token，token只要不为123，即token不为空就会刷新ui
            if (it != "123") {
                refreshUi()
            }
        }
        viewModel.userInfoResponse.observe(viewLifecycleOwner){
            mTvCoin.text = it.info.experienceNum.toString()
            mTvFollow.text = it.info.attentionNum.toString()
            mTvFollowers.text = it.info.fansNum.toString()
            mTvUserName.text = it.user.nickname
            Glide.with(this).load(it.user.avatar).into(mImgUser)
            //当请求成功并且token未过期的时候，变为false
            viewModel.isNeedToken = false
        }
    }

    private fun initUi(){
        mTvUserName.text = "登陆/注册"
        Glide.with(this).load("https://jokes-avatar.oss-cn-beijing.aliyuncs.com/aliyun/jokes/avatar/default_avatar.png")
            .into(mImgUser)
        mTvCoin.text = "0"
        mTvFollow.text = "0"
        mTvFollowers.text = "0"
    }

    override fun onResume() {
        super.onResume()
        //切换回前台后，刷新token，主要目的为登陆成功后，刷新用户信息
        if (viewModel.isNeedToken) viewModel.refreshToken()
    }

}