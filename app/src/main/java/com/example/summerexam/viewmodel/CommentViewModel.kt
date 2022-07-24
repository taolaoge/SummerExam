package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.Comment
import com.example.summerexam.services.CommentService
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.throwApiExceptionIfFail
import com.example.summerexam.extensions.toast
import com.example.summerexam.extensions.unSafeSubscribeBy
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/16
 */
class CommentViewModel : ViewModel() {

    val newData = ArrayList<Comment>()
    var oldData = ArrayList<Comment>()
    var id = 0
    var count = 0

    private val _needRefresh = MutableLiveData<Boolean>()
    val needRefresh: LiveData<Boolean>
        get() = _needRefresh

    private val _commentSuccess = MutableLiveData<Boolean>()
    val commentSuccess: LiveData<Boolean>
        get() = _commentSuccess

    private val _code = MutableLiveData<Int>()
    val code: LiveData<Int>
        get() = _code

    fun getCommentList() {
        _commentSuccess.value = false
        CommentService.INSTANCE.getCommentList(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                oldData = newData
                for (i in it.comments) {
                    newData.add(i)
                }
                count = it.count
                _commentSuccess.value = true
            }
    }

    fun likeComment(id: String, status: Boolean,position:Int) {
        CommentService.INSTANCE.likeComment(id, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
            .unSafeSubscribeBy {
                if (it.code == 200){
                    toast("操作成功")
                    newData[position].isLike = status
                    if (status){
                        newData[position].likeNum += 1
                    }else{
                        newData[position].likeNum -= 1
                    }
                    _needRefresh.value = true
                }
            }
    }

    fun changeNeedRefresh(){
        _needRefresh.value = false
    }
}