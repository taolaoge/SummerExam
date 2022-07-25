package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.summerexam.baseui.BaseViewModel
import com.example.summerexam.beans.TiktokResponseItem
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.services.TiktokService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/24
 */
class FindingViewModel : BaseViewModel() {
    private val _tiktokList = MutableLiveData<List<TiktokResponseItem>>()
    val tiktokList: LiveData<List<TiktokResponseItem>>
        get() = _tiktokList

    init {
        getTiktokList()
    }

    fun getTiktokList() {
       TiktokService.INSTANCE.getTiktokList()
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .mapOrThrowApiException()
           .safeSubscribeBy {
               _tiktokList.value = buildList {
                   addAll(_tiktokList.value?: emptyList())
                   addAll(it)
               }
           }
    }
}