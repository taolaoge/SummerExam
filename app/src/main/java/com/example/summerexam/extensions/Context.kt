package com.ndhzs.lib.common.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.summerexam.BaseApp

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/30 15:57
 */

val appContext
  get() = BaseApp.appContext

inline fun <reified T : Activity> Context.startActivity() {
  val intent = Intent(this, T::class.java)
  if (this !is Activity) {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  }
  startActivity(intent)
}