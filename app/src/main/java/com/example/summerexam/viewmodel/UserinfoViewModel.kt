package com.example.summerexam.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.FirstTextResponse
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.beans.TargetUserinfoResponse
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

    lateinit var targetUserinfoResponse: TargetUserinfoResponse

    var userId = ""



    fun getTargetUserinfo(userId:String,block:()->Unit){
        UserinfoService.INSTANCE.getTargetUserInfo(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                targetUserinfoResponse = it
                block()
            }
    }

    fun followUser(status: String, userId: String, block: (Boolean) -> Unit) {
        FirstRepository.followUser(status, userId)
            .unSafeSubscribeBy {
                if (it.code == 200) block(true)
            }
    }

}