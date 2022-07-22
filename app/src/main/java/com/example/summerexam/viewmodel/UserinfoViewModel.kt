package com.example.summerexam.viewmodel

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

    val page = MutableLiveData(1)

    //新的数据集合，差分刷新使用
    val newTextData = ArrayList<FirstTextResponseItem>()
    val oldTextData = ArrayList<FirstTextResponseItem>()

    var userId = ""

    val isLoading = MutableLiveData(true)

    var targetUserinfoResponse: TargetUserinfoResponse? = null
    var userinfoResponse:UserInfoResponse? = null



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

    fun getUserinfo(block: () -> Unit){
        UserinfoService.INSTANCE.getUserinfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                userinfoResponse = it
                block()
            }
    }

    fun followUser(status: String, userId: String, block: (Boolean) -> Unit) {
        FirstRepository.followUser(status, userId)
            .unSafeSubscribeBy {
                if (it.code == 200) block(true)
            }
    }

    fun getUserJoke() {
        UserinfoService.INSTANCE.getUserJoke(userId, page.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                dealData(it)
            }
    }


    fun likeJoke(id: Int, status: Boolean, block: (Boolean) -> Unit) {
        FirstRepository.likeJoke(id, status)
            .unSafeSubscribeBy {
                if (it.code == 200) block(true)
            }
    }

    fun dislikeJoke(id: Int, status: Boolean, block: (Boolean) -> Unit) {
        FirstRepository.dislikeJoke(id, status)
            .unSafeSubscribeBy {
                if (it.code == 200) block(true)
            }
    }


    private fun dealData(it: FirstTextResponse) {
        for (data in it) {
            newTextData.add(data)
        }
        isLoading.value = false
        page.value = page.value?.plus(1)
    }
}