package com.example.summerexam.utils

import android.view.View
import android.widget.FrameLayout

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/18
 */
object DkVideoPlayerUtils {
    /**
     * 将View从父控件中移除
     */
    fun removeViewFormParent(v: View?) {
        if (v == null) return
        val parent = v.parent
        if (parent is FrameLayout) {
            parent.removeView(v)
        }
    }
}