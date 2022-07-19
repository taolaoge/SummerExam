package com.example.summerexam.ui.activities

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.summerexam.R
import com.example.summerexam.baseui.BaseActivity

class SearchActivity : BaseActivity() {
    private val mEdSearch by R.id.ed_search.view<EditText>()
    private val mTvBack by R.id.tv_search_back.view<TextView>()
    private lateinit var mFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

    }
}