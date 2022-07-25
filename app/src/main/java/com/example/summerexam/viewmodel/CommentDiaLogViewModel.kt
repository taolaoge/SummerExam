package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.baseui.BaseViewModel
import com.example.summerexam.services.CommentService
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.toast
import com.example.summerexam.extensions.unSafeSubscribeBy
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/18
 */
class CommentDiaLogViewModel : BaseViewModel() {

    private val _commentSuccess = MutableLiveData<Boolean>()
    val commentSuccess :LiveData<Boolean>
    get() = _commentSuccess

    var jokeId = 0
    fun commentJoke(content: String, jokeId: String) {
        _commentSuccess.value = false
        CommentService.INSTANCE.commentJoke(content, jokeId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .safeSubscribeBy {
                toast("评论成功")
                _commentSuccess.value = true
            }
    }
}