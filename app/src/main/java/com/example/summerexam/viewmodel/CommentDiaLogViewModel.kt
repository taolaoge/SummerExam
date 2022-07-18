package com.example.summerexam.viewmodel

import androidx.lifecycle.ViewModel
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
 * date : 2022/7/18
 */
class CommentDiaLogViewModel : ViewModel() {
    var jokeId = 0
    fun commentJoke(content: String, jokeId: String,block:()->Unit) {
        CommentService.INSTANCE.commentJoke(content, jokeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                toast("评论成功")
                block()
            }
    }
}