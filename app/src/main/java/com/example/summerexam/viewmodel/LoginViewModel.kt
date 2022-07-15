package com.example.summerexam.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.BaseApp
import com.example.summerexam.network.*
import com.example.summerexam.services.LoginService
import com.ndhzs.lib.common.extensions.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
class LoginViewModel : ViewModel() {
    private val _token = MutableLiveData("123")
    val token:MutableLiveData<String>
        get() = _token

    fun login(code: String, phone: String,block:()->Unit) {
        LoginService.INSTANCE.login(code, phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                appContext.getSp("token").edit {
                    putString("token", it.token)
                }
                block()
            }
    }

    fun getCode(phone: String) {
        LoginService.INSTANCE.getCode(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy { }
    }
}