package com.example.summerexam.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.summerexam.R
import com.example.summerexam.adapters.MainFragmentAdapter
import com.example.summerexam.ui.fragments.first.FirstFragment
import com.example.summerexam.ui.fragments.FindingFragment
import com.example.summerexam.ui.fragments.MineFragment
import com.example.summerexam.ui.fragments.MessageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.summerexam.baseui.BaseActivity
import com.example.summerexam.extensions.startActivity

class MainActivity : BaseActivity() {
    private val mBottomNavigation by R.id.main_bottom_navigation.view<BottomNavigationView>()

    private val mViewPager by R.id.main_vp2.view<ViewPager2>()
        .addInitialize {
            isUserInputEnabled = false
        }

    private var fragments = arrayListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragmentData()
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        mBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_first -> {
                    mViewPager.setCurrentItem(0,false)
                }
                R.id.bottom_finding -> {
                    mViewPager.setCurrentItem(1,false)
                }
                R.id.bottom_message -> {
                    mViewPager.setCurrentItem(2,false)
                }
                R.id.bottom_mine -> {
                    mViewPager.setCurrentItem(3,false)
                }
            }
            true
        }
    }

    private fun initFragmentData() {
        fragments.run {
            add(FirstFragment())
            add(FindingFragment())
            add(MessageFragment())
            add(MineFragment())
        }
        mViewPager.adapter = MainFragmentAdapter(this@MainActivity, fragments)
    }
}