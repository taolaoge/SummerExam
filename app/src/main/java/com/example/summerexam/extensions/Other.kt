package com.ndhzs.lib.common.extensions

import com.example.summerexam.BaseApp


/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/3/7 17:51
 */

/**
 * 不带锁的懒加载，建议使用这个代替 lazy，因为 Android 一般情况下不会遇到多线程问题
 */
fun <T> lazyUnlock(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun Int.dp2pxF(): Float = BaseApp.appContext.resources.displayMetrics.density * this
fun Int.dp2px(): Int = dp2pxF().toInt()

fun Float.dp2pxF(): Float = BaseApp.appContext.resources.displayMetrics.density * this
fun Float.dp2px(): Int = dp2pxF().toInt()

fun Int.dp2spF(): Float = BaseApp.appContext.resources.displayMetrics.scaledDensity * this
fun Int.dp2sp(): Float = dp2spF() * this

fun Float.dp2spF(): Float = BaseApp.appContext.resources.displayMetrics.scaledDensity * this
fun Float.dp2sp(): Float = dp2spF() * this
