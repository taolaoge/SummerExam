package com.example.summerexam.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/26
 */
class MyLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context) {

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }
}