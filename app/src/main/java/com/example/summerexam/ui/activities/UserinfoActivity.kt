package com.example.summerexam.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.summerexam.R
import com.example.summerexam.adapters.FirstFragmentAdapter
import com.example.summerexam.adapters.UserInfoFragmentVpAdapter
import com.example.summerexam.baseui.BaseActivity
import com.example.summerexam.ui.fragments.UserinfoJokeFragment
import com.example.summerexam.ui.fragments.first.FirstTextFragment
import com.example.summerexam.viewmodel.UserinfoViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class UserinfoActivity : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[UserinfoViewModel::class.java] }
    private val mViewPager by R.id.vp2_userinfo.view<ViewPager2>()

    private val mTabLayout by R.id.tab_layout_userinfo.view<TabLayout>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        viewModel.userId = intent.getStringExtra("userId") ?: ""
        initViewPager()
    }

    private fun initViewPager() {
        val fragments = arrayListOf(
            FirstTextFragment(),
            FirstTextFragment()
        ).onEachIndexed { index, userinfoJokeFragment ->
            userinfoJokeFragment.arguments = Bundle().apply {
                putInt("page", index+6)
                putString("userId", viewModel.userId)
            }
        }
        val data = arrayOf("作品", "喜欢")
        mViewPager.adapter = UserInfoFragmentVpAdapter(this, fragments)
        mViewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = data[position]
        }.attach()
    }
}