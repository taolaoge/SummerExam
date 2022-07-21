package com.example.summerexam.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.summerexam.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        val webView:WebView = findViewById(R.id.webview)
        webView.settings.run {
            useWideViewPort = true
            loadWithOverviewMode = true //设置加载进来的页面自适应手机屏幕
        }
        val url = intent.getStringExtra("url")
        if (url != null) webView.loadUrl(url)
    }
}