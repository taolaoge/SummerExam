package com.example.summerexam.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.FirstTextResponse
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.unSafeSubscribeBy
import com.example.summerexam.services.UserinfoService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/20
 */
class UserJokeViewModel : ViewModel() {
    val page = MutableLiveData(1)
    val position = MutableLiveData<Int>()

    //新的数据集合，差分刷新使用
    val newTextData = ArrayList<FirstTextResponseItem>()
    var oldTextData = ArrayList<FirstTextResponseItem>()

    var userId = ""

    val isLoading = MutableLiveData(true)


    fun getList(){
        if (position.value == 0) getUserJoke()
        else getUserLikeJoke()
    }

    private fun getUserJoke() {
        UserinfoService.INSTANCE.getUserJoke(userId, page.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                dealData(it)
            }
    }

    private fun getUserLikeJoke() {
        UserinfoService.INSTANCE.getUserLikeJoke(userId, page.value ?: 1)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                dealData(it)
            }
    }

    private fun dealData(it: FirstTextResponse) {
        oldTextData = newTextData
        for (data in it) {
            newTextData.add(data)
        }
        isLoading.value = false
        //请求成功后参数变为false，因为观察了这个数据，达到一个回调的目的
        page.value = page.value?.plus(1)
    }
}