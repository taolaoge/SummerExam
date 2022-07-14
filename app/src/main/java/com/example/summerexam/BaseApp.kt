package com.example.summerexam

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper


/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/26 14:01
 */
open class BaseApp : Application() {
  companion object {
    lateinit var appContext: Context
      private set
  }
  
  @CallSuper
  override fun onCreate() {
    super.onCreate()
    appContext = this
  }

}