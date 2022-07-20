package com.example.summerexam.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.SystemMessageResponseItem
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.unSafeSubscribeBy
import com.example.summerexam.services.MessageService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/20
 */
class MessageViewModel : ViewModel() {

    var oldData = ArrayList<SystemMessageResponseItem>()
    val newData = ArrayList<SystemMessageResponseItem>()

    val page = MutableLiveData(1)

    fun getSystemMessage(block:()->Unit) {
        MessageService.INSTANCE.getMessageSystem(page.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                oldData = newData
                for (data in it) newData.add(data)
                block()
            }
    }
}