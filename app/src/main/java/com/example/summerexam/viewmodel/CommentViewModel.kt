package com.example.summerexam.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.Comment
import com.example.summerexam.network.TAG
import com.example.summerexam.services.CommentService
import com.ndhzs.lib.common.extensions.mapOrThrowApiException
import com.ndhzs.lib.common.extensions.toast
import com.ndhzs.lib.common.extensions.unSafeSubscribeBy
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/16
 */
class CommentViewModel : ViewModel() {

    val data = ArrayList<Comment>()
    var id = 0
    var count = 0

    fun getCommentList(block:() -> Unit) {
        CommentService.INSTANCE.getCommentList(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .doOnError {
                toast(it.toString())
            }
            .unSafeSubscribeBy {
                for (i in it.comments) {
                    data.add(i)
                }
                count = it.count
                block()
            }
    }
}