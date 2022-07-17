package com.example.summerexam.fragments.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.summerexam.R
import com.example.summerexam.adapters.FirstFragmentAdapter
import com.example.summerexam.viewmodel.FirstViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ndhzs.lib.common.ui.BaseFragment


class FirstFragment : BaseFragment() {
    private val viewModel by lazy { ViewModelProvider(this)[FirstViewModel::class.java] }
    private val mViewPager by R.id.vp2_first.view<ViewPager2>()
        .addInitialize {
            val child: View = getChildAt(0)
            if (child is RecyclerView) {
                child.setOverScrollMode(View.OVER_SCROLL_NEVER)
            }
        }
    private val mTabLayout by R.id.tab_layout_first.view<TabLayout>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        val bundleList = ArrayList<Bundle>()
        for (i in 0..4){
            val bundle = Bundle()
            bundle.putInt("page",i)
            bundleList.add(bundle)
        }
        val followFragment = FollowFragment()
        val recommendFragment = RecommendFragment()
        val freshFragment = FreshFragment()
        val onlyTextFragment = OnlyTextFragment()
        val pictureFragment = OnlyTextFragment()
        followFragment.arguments = bundleList[0]
        recommendFragment.arguments = bundleList[1]
        freshFragment.arguments = bundleList[2]
        onlyTextFragment.arguments = bundleList[3]
        pictureFragment.arguments = bundleList[4]
        val fragments = arrayListOf<Fragment>(
            followFragment,
            recommendFragment,
            freshFragment,
            onlyTextFragment,
            pictureFragment
        )
        val data = arrayOf("关注","推荐","新鲜","纯文","趣图")
        mViewPager.adapter = FirstFragmentAdapter(this,fragments)
        TabLayoutMediator(mTabLayout,mViewPager){tab,position->
            tab.text = data[position]
        }.attach()
    }
}