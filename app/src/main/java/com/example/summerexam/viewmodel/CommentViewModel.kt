package com.example.summerexam.viewmodel

import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.Comment
import com.example.summerexam.services.CommentService
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.throwApiExceptionIfFail
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

    fun getCommentList(block:() -> Unit) {
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
                block()
            }
    }

    fun likeComment(id:String,status:Boolean,block: (Boolean) -> Unit){
        CommentService.INSTANCE.likeComment(id,status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
            .unSafeSubscribeBy {
                if (it.code == 200) block(true)
            }
    }
}