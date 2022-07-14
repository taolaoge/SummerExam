package com.example.summerexam.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
class FragmentAdapter(
    private val fragmentActivity: FragmentActivity,
    private val fragments: ArrayList<Fragment>
) : FragmentStateAdapter(
    fragmentActivity
) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}