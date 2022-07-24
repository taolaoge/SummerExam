package com.example.summerexam.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.summerexam.R
import com.example.summerexam.baseui.BaseActivity
import com.example.summerexam.ui.fragments.MyCommentFragment
import com.example.summerexam.ui.fragments.first.FirstTextFragment

class MyLikeJokeActivity : BaseActivity() {
    private val toolbar by R.id.toolbar_userinfo.view<Toolbar>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like_joke)
        when(intent.getIntExtra("extra",-1)){
            1 -> {
                val fragment = FirstTextFragment()
                fragment.arguments = Bundle().apply {putInt("page",7)}
                toolbar.title = "我喜欢的"
                supportFragmentManager.beginTransaction().replace(
                    R.id.fl_fragment_container,fragment
                ).commit()
            }
            2 -> {
                val fragment = MyCommentFragment()
                supportFragmentManager.beginTransaction().replace(
                    R.id.fl_fragment_container,fragment
                ).commit()
                toolbar.title = "我评论的"
            }
        }

    }
}