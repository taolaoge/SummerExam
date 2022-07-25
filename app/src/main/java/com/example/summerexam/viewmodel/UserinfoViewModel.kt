package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.baseui.BaseViewModel
import com.example.summerexam.beans.FirstTextResponse
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.beans.TargetUserinfoResponse
import com.example.summerexam.beans.UserInfoResponse
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.toast
import com.example.summerexam.extensions.unSafeSubscribeBy
import com.example.summerexam.repository.FirstRepository
import com.example.summerexam.services.UserinfoService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.NullPointerException

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/20
 */
class UserinfoViewModel : BaseViewModel() {

    private val _page = MutableLiveData(1)
    val page: LiveData<Int>
        get() = _page

    private val _code = MutableLiveData<Int>()
    val code: LiveData<Int>
        get() = _code

    private val _followSuccess = MutableLiveData<Int>()
    val followSuccess: LiveData<Int>
        get() = _followSuccess

    private val _needRefresh = MutableLiveData<Boolean>()
    val needRefresh: LiveData<Boolean>
        get() = _needRefresh

    var freshPosition = 0

    //新的数据集合，差分刷新使用
    val newTextData = ArrayList<FirstTextResponseItem>()
    val oldTextData = ArrayList<FirstTextResponseItem>()

    var userId = ""

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _targetUserinfoResponse = MutableLiveData<TargetUserinfoResponse>()
    val targetUserinfoResponse: LiveData<TargetUserinfoResponse>
        get() = _targetUserinfoResponse


    fun getTargetUserinfo(userId: String) {
        UserinfoService.INSTANCE.getTargetUserInfo(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .safeSubscribeBy {
                _targetUserinfoResponse.value = it
            }
    }

    fun followUser(status: String, userId: String) {
        _followSuccess.value = -1
        FirstRepository.followUser(status, userId)
            .safeSubscribeBy {
                _code.value = it.code
                if (status == "1") _followSuccess.value = 1
                else _followSuccess.value = 0
            }
    }

    fun getUserJoke() {
        UserinfoService.INSTANCE.getUserJoke(userId, _page.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .safeSubscribeBy {
                dealData(it)
            }
    }


    fun likeJoke(id: Int, status: Boolean, position: Int) {
        FirstRepository.likeJoke(id, status)
            .safeSubscribeBy {
                if (it.code == 200) {
                    freshPosition = position
                    newTextData[position].info.isLike = status
                    if (status) {
                        newTextData[position].info.likeNum += 1
                    } else {
                        newTextData[position].info.likeNum -= 1
                    }
                    _needRefresh.value = true
                    toast("请求成功")
                }
            }
    }

    fun dislikeJoke(id: Int, status: Boolean, position: Int) {
        FirstRepository.dislikeJoke(id, status)
            .safeSubscribeBy {
                if (it.code == 200) {
                    freshPosition = position
                    newTextData[position].info.isUnlike = status
                    if (status) {
                        newTextData[position].info.disLikeNum += 1
                    } else {
                        newTextData[position].info.disLikeNum -= 1
                    }
                    _needRefresh.value = true
                    toast("请求成功")
                }
            }
    }

    private fun dealData(it: FirstTextResponse) {
        for (data in it) {
            newTextData.add(data)
        }
        _isLoading.value = false
        _page.value = _page.value?.plus(1)
    }


    fun changeNeedRefresh(){
        _needRefresh.value = false
    }
}