package com.example.summerexam.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class FirstFragmentAdapter(
    private val fragmentActivity: Fragment,
    private val fragments: List<Fragment>
    ) : FragmentStateAdapter(
    fragmentActivity
    ) {
        override fun getItemCount(): Int = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
}