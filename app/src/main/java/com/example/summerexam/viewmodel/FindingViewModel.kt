package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.summerexam.baseui.BaseViewModel
import com.example.summerexam.beans.TiktokResponseItem
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.toast
import com.example.summerexam.repository.FirstRepository
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


    private val _needRefresh = MutableLiveData<Boolean>()
    val needRefresh: LiveData<Boolean>
        get() = _needRefresh

    var freshPosition = 0

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
                    addAll(_tiktokList.value ?: emptyList())
                    addAll(it)
                }
            }
    }

    fun likeJoke(position: Int) {
        val id = _tiktokList.value?.get(position)?.joke?.jokesId ?: 1
        val status1 = _tiktokList.value?.get(position)?.info?.isLike ?: false
        val status = !status1
        FirstRepository.likeJoke(id, status)
            .safeSubscribeBy {
                if (it.code == 200) {
                    if (!status) {
                        _tiktokList.value?.get(position)?.info?.likeNum =
                            _tiktokList.value?.get(position)?.info?.likeNum?.minus(1) ?: 0
                        _tiktokList.value?.get(position)?.info?.isLike = status
                    } else {
                        _tiktokList.value?.get(position)?.info?.likeNum =
                            _tiktokList.value?.get(position)?.info?.likeNum?.plus(1) ?: 0
                        _tiktokList.value?.get(position)?.info?.isLike = status
                    }
                    toast("操作成功")
                    freshPosition = position
                    _needRefresh.value = true
                }
            }
    }

    fun changeNeedRefresh() {
        _needRefresh.value = false
    }
}