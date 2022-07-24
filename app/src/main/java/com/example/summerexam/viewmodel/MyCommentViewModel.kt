package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.summerexam.baseui.BaseViewModel
import com.example.summerexam.beans.MyCommentResponseItem
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.services.UserinfoService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/24
 */
class MyCommentViewModel : BaseViewModel() {
    private val _commentList = MutableLiveData<List<MyCommentResponseItem>>()
    val commentList: LiveData<List<MyCommentResponseItem>>
        get() = _commentList

    private val _page = MutableLiveData<Int>(1)
    val page: LiveData<Int>
        get() = _page

    var isLoading = false

    fun getCommentList() {
        isLoading = true
        UserinfoService.INSTANCE.getMineComment(page.value?:1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .safeSubscribeBy {
                _commentList.value = buildList {
                    addAll(_commentList.value?: emptyList())
                    addAll(it)
                }
                isLoading = false
            }
    }

    fun changePage(){
        _page.value = _page.value?.plus(1)
    }
}