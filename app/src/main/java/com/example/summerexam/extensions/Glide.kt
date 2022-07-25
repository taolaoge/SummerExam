package com.example.summerexam.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/25
 */

fun ImageView.glide(url: String, func: (RequestBuilder<Drawable>.() -> Unit)? = null) {
    Glide.with(this)
        .load(url)
        .apply { func?.invoke(this) }
        .into(this)
}