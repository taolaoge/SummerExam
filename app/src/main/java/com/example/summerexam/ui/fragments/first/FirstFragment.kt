package com.example.summerexam.ui.fragments.first

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.summerexam.R
import com.example.summerexam.ui.activities.SearchActivity
import com.example.summerexam.adapters.FirstFragmentAdapter
import com.example.summerexam.viewmodel.FirstViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.summerexam.baseui.BaseFragment


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
    private val mImgSearch by R.id.img_search.view<ImageView>()
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
        mImgSearch.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initViewPager() {
        val fragments = arrayListOf(
            FirstTextFragment(),
            FirstTextFragment(),
            FirstTextFragment(),
            FirstTextFragment(),
            FirstTextFragment(),
        ).onEachIndexed { index, fragment ->
            fragment.arguments = Bundle().apply{putInt("page", index) }
        }
        val data = arrayOf("关注", "推荐", "新鲜", "纯文", "趣图")
        mViewPager.adapter = FirstFragmentAdapter(this, fragments)
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = data[position]
        }.attach()
    }
}