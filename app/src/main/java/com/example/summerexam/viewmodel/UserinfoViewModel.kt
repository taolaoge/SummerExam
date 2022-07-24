package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.FirstTextResponse
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.beans.TargetUserinfoResponse
import com.example.summerexam.beans.UserInfoResponse
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.unSafeSubscribeBy
import com.example.summerexam.repository.FirstRepository
import com.example.summerexam.services.UserinfoService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/20
 */
class UserinfoViewModel : ViewModel() {

    private val _page = MutableLiveData(1)
    val page: LiveData<Int>
        get() = _page

    private val _code = MutableLiveData<Int>()
    val code: LiveData<Int>
        get() = _code

    private val _followSuccess = MutableLiveData<Int>()
    val followSuccess: LiveData<Int>
        get() = _followSuccess

    //新的数据集合，差分刷新使用
    val newTextData = ArrayList<FirstTextResponseItem>()
    val oldTextData = ArrayList<FirstTextResponseItem>()

    var userId = ""

    val isLoading = MutableLiveData(true)


    private val _targetUserinfoResponse = MutableLiveData<TargetUserinfoResponse>()
    val targetUserinfoResponse: LiveData<TargetUserinfoResponse>
        get() = _targetUserinfoResponse


    fun getTargetUserinfo(userId: String) {
        UserinfoService.INSTANCE.getTargetUserInfo(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                _targetUserinfoResponse.value = it
            }
    }

    fun followUser(status: String, userId: String) {
        _followSuccess.value = -1
        FirstRepository.followUser(status, userId)
            .unSafeSubscribeBy {
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
            .unSafeSubscribeBy {
                dealData(it)
            }
    }


    fun likeJoke(id: Int, status: Boolean) {
        FirstRepository.likeJoke(id, status)
            .unSafeSubscribeBy {
                _code.value = it.code
            }
    }

    fun dislikeJoke(id: Int, status: Boolean) {
        FirstRepository.dislikeJoke(id, status)
            .unSafeSubscribeBy {
                _code.value = it.code
            }
    }


    private fun dealData(it: FirstTextResponse) {
        for (data in it) {
            newTextData.add(data)
        }
        isLoading.value = false
        _page.value = _page.value?.plus(1)
    }

    fun changeCode(){
        _code.value = -1
    }
}