package com.example.summerexam.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.services.MineService
import com.ndhzs.lib.common.extensions.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class MineViewModel : ViewModel() {
    private val _token = MutableLiveData("123")
    val token: MutableLiveData<String>
        get() = _token
    //是否需要刷新ui，是否需要重新获取token，这个变量来控制获取到最新token后再次拿到token
    var isNeedRefresh = true

    var follow = 0
    var followers = 0
    var coin = 0
    var username = ""

    fun refreshToken(){
        _token.value = appContext.getSp("token").getString("token","123")
    }

    fun refreshUi(block:() -> Unit){
        MineService.INSTANCE.getUserInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .doOnError {
                toast(it.toString())
            }
            .unSafeSubscribeBy {
                follow = it.info.attentionNum
                followers = it.info.fansNum
                coin = it.info.experienceNum
                username = it.user.nickname
                block()
            }
    }
}