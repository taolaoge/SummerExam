package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.baseui.BaseViewModel
import com.example.summerexam.beans.UserInfoResponse
import com.example.summerexam.extensions.*
import com.example.summerexam.services.MineService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class MineViewModel : BaseViewModel() {
    private val _token = MutableLiveData("123")
    val token: MutableLiveData<String>
        get() = _token

    //是否需要刷新ui，是否需要重新获取token，这个变量来控制获取到最新token后再次拿到token
    var isNeedToken = true

    private val _userInfoResponse = MutableLiveData<UserInfoResponse>()
    val userInfoResponse: LiveData<UserInfoResponse>
        get() = _userInfoResponse

    fun refreshToken() {
        _token.value = appContext.getSp("token").getString("token", "123")
    }

    fun refreshUi() {
        MineService.INSTANCE.getUserInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .doOnError {
                toast(it.toString())
            }
            .safeSubscribeBy {
                _userInfoResponse.value = it
            }
    }
}