package com.example.summerexam.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.baseui.BaseViewModel
import com.example.summerexam.extensions.*
import com.example.summerexam.services.LoginService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
class LoginViewModel : BaseViewModel() {
    private val _needFinish = MutableLiveData<Boolean>()
    val needFinish:LiveData<Boolean>
    get() = _needFinish

    fun login(code: String, phone: String) {
        LoginService.INSTANCE.login(code, phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .safeSubscribeBy {
                appContext.getSp("token").edit {
                    putString("token", it.token)
                }
                _needFinish.value = true
            }
    }

    fun getCode(phone: String) {
        LoginService.INSTANCE.getCode(phone)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
            .safeSubscribeBy {
                if (it.code != 200) toast(it.msg)
                else toast("请求成功")
            }
    }
}