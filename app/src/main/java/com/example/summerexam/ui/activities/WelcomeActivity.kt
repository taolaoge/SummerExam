package com.example.summerexam.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.summerexam.R
import com.example.summerexam.baseui.BaseActivity

class WelcomeActivity : BaseActivity() {
    private val mImgBg by R.id.img_welcome_bg.view<ImageView>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        mImgBg.setBackgroundResource(R.drawable.ic_return1)
        startAnimation()
    }

    private fun startAnimation() {

    }
}