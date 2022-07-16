package com.example.summerexam.viewmodel

import android.text.BoringLayout
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.OnlyTextResponseItem
import com.example.summerexam.network.TAG
import com.example.summerexam.services.OnlyTextService
import com.ndhzs.lib.common.extensions.mapOrThrowApiException
import com.ndhzs.lib.common.extensions.throwApiExceptionIfFail
import com.ndhzs.lib.common.extensions.toast
import com.ndhzs.lib.common.extensions.unSafeSubscribeBy
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class OnlyTextViewModel : ViewModel() {
    val newTextData = ArrayList<OnlyTextResponseItem>()
    var oldTextData = ArrayList<OnlyTextResponseItem>()

    /**
     * 判断是否正在请求数据
     * 因为我是用的监听最后一个item来继续请求更多数据，如果没有这个参数，很有可能重复请求两次数据
     */
    val isLoading = MutableLiveData(true)

    /**
     * 监听 判断是否请求数据完成，如果请求数据已完成，此参数变为false
     * 监听此参数的就会被回调，取消SwipeLayout的动画
     * 其实接口回调和高阶函数更好，但是这里没有page参数，不好判断是否回调
     */
    val isSwipeLayoutRefreshing = MutableLiveData(false)

    fun getOnlyText() {
        isLoading.value = true
        OnlyTextService.INSTANCE.getOnlyText()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
            .unSafeSubscribeBy {
                oldTextData = newTextData
                for (data in it) {
                    newTextData.add(data)
                }
                isLoading.value = false
                //请求成功后参数变为false，因为观察了这个数据，达到一个回调的目的
                isSwipeLayoutRefreshing.value = false
            }
    }

    fun likeJoke(id:Int, status:Boolean,block:(Boolean) -> Unit){
        OnlyTextService.INSTANCE.likeJoke(id, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
            .unSafeSubscribeBy {
                if (it.code == 200) block(true)
            }
    }

    fun dislikeJoke(id:Int,status: Boolean,block:(Boolean) -> Unit){
        OnlyTextService.INSTANCE.dislikeJoke(id, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
            .doOnError {
                toast(it.toString())
            }
            .unSafeSubscribeBy {
                if (it.code == 200) block(true)
            }
    }

    fun clearList(){
        newTextData.clear()
        oldTextData.clear()
        //下拉刷新数据时，先将此参数变为true，意味着正在请求
        isSwipeLayoutRefreshing.value = true
        getOnlyText()
    }
}