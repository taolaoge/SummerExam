package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.summerexam.baseui.BaseViewModel
import com.example.summerexam.beans.AttentionListResponseItem
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.services.UserinfoService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/25
 */
class AttentionListViewModel : BaseViewModel() {
    private val _attentionList = MutableLiveData<List<AttentionListResponseItem>>()
    val attentionList: LiveData<List<AttentionListResponseItem>>
        get() = _attentionList

    var userId = ""

    var isLoading = false

    private val _page = MutableLiveData(1)
    val page: LiveData<Int>
        get() = _page

    fun getAttentionList(){
        isLoading = true
        UserinfoService.INSTANCE.getAttentionList(page.value?:1,userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .safeSubscribeBy() {
                _attentionList.value = buildList {
                    addAll(attentionList.value?: emptyList())
                    addAll(it)
                }
                isLoading = false
            }
    }


    fun changePage(){
        _page.value = _page.value?.plus(1)
    }
}