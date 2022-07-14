package com.example.summerexam.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.summerexam.R
import com.example.summerexam.adapters.FragmentAdapter
import com.example.summerexam.fragments.FirstFragment
import com.example.summerexam.fragments.FindingFragment
import com.example.summerexam.fragments.MineFragment
import com.example.summerexam.fragments.MessageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ndhzs.lib.common.ui.BaseActivity

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
        mViewPager.adapter = FragmentAdapter(this@MainActivity, fragments)
    }
}