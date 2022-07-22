package com.example.summerexam.ui.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.summerexam.R
import com.example.summerexam.baseui.BaseActivity
import com.example.summerexam.ui.fragments.UserinfoJokeFragment
import com.example.summerexam.ui.fragments.first.FirstTextFragment
import com.example.summerexam.viewmodel.UserinfoViewModel

class UserinfoActivity : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[UserinfoViewModel::class.java] }
    private val mImgAvatar by R.id.img_userinfo_avatar.view<ImageView>()
    private val mTvNickname by R.id.tv_userinfo_nickname.view<TextView>()
    private val mTvJoinTime by R.id.tv_userinfo_jointime.view<TextView>()
    private val mTvSignature by R.id.tv_userinfo_signature.view<TextView>()
    private val mTvFabulous by R.id.tv_userinfo_fabulous.view<TextView>()
    private val mTvFollow by R.id.tv_userinfo_following.view<TextView>()
    private val mTvFollowers by R.id.tv_userinfo_followers.view<TextView>()
    private val mImgBackground by R.id.img_userinfo_background.view<ImageView>()
    private val mImgReturn by R.id.img_userinfo_return.view<ImageView>()
    private val mBtnFollow by R.id.btn_userinfo.view<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        viewModel.userId = intent.getStringExtra("userId") ?: ""
        initFragment()
        viewModel.getTargetUserinfo(viewModel.userId) {
            viewModel.targetUserinfoResponse.run {
                mTvFabulous.text = "$likeNum 获赞"
                mTvNickname.text = nickname
                mTvJoinTime.text = "入驻段子乐 $joinTime"
                mTvSignature.text = sigbature
                mTvFollow.text = "$attentionNum 关注"
                mTvFollowers.text = "$fansNum 粉丝"
                Glide.with(this@UserinfoActivity).load(avatar).into(mImgAvatar)
                Glide.with(this@UserinfoActivity).load(avatar).into(mImgBackground)
                if (attentionState == 0) {
                    mBtnFollow.text = "+关注"
                } else  {
                    mBtnFollow.text = "已关注"
                    mBtnFollow.setBackgroundResource(R.drawable.shape_btn_follow)
                }
            }
        }
        mImgReturn.setOnClickListener { finish() }
        mBtnFollow.setOnClickListener{clickFollow()}
    }

    private fun clickFollow() {
        if (viewModel.targetUserinfoResponse.attentionState == 0)
            viewModel.followUser("1",viewModel.userId){
                viewModel.targetUserinfoResponse.attentionState = 1
                mBtnFollow.text = "已关注"
                mBtnFollow.setBackgroundResource(R.drawable.shape_btn_follow)
            }
        else viewModel.followUser("0",viewModel.userId){
            viewModel.targetUserinfoResponse.attentionState = 0
            mBtnFollow.text = "+关注"
            mBtnFollow.setBackgroundResource(R.drawable.shape_btn)
        }
    }

    private fun initFragment() {
        val fragment = UserinfoJokeFragment()
        fragment.arguments = Bundle().apply {
            putString("userId", viewModel.userId)
        }
        supportFragmentManager.beginTransaction().replace(R.id.fl_userinfo_container, fragment)
            .commit()
    }
}