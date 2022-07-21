package com.example.summerexam.ui.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.baseui.BaseActivity
import com.example.summerexam.ui.fragments.first.FirstTextFragment
import com.example.summerexam.viewmodel.FirstTextViewModel

class SearchActivity : BaseActivity() {
    private val viewModel by lazy { ViewModelProvider(this)[FirstTextViewModel::class.java] }
    private val mEdSearch by R.id.ed_search.view<EditText>()
    private val mTvSearch by R.id.tv_search.view<TextView>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mTvSearch.setOnClickListener{
            viewModel.keyword.value = mEdSearch.text.toString()
            val fragment = FirstTextFragment()
            fragment.arguments = Bundle().apply {
                putString("keyword",mEdSearch.text.toString())
                putInt("page",6)
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fl_search,fragment).commit()
        }
    }
}