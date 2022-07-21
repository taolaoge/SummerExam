package com.example.summerexam.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.beans.AttentionRecommendResponse
import com.example.summerexam.beans.AttentionRecommendResponseItem
import com.example.summerexam.beans.FirstTextResponse
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.extensions.*
import com.example.summerexam.network.TAG
import com.example.summerexam.repository.FirstRepository
import com.example.summerexam.services.FirstTextService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class FirstTextViewModel : ViewModel() {
    val token = MutableLiveData(appContext.getSp("token").getString("token","123"))
    private val attentionPage = MutableLiveData(0)

    val keyword = MutableLiveData<String>()
    var userId = ""

    val noTokenIsLoading = MutableLiveData(false)
    //关注fragment中推荐关注的用户列表
    val newRecommendUserData = ArrayList<AttentionRecommendResponseItem>()
    var oldRecommendUserData = ArrayList<AttentionRecommendResponseItem>()

    //新的数据集合，差分刷新使用
    val newTextData = ArrayList<FirstTextResponseItem>()
    var oldTextData = ArrayList<FirstTextResponseItem>()

    val page = MutableLiveData<Int>()

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
    val isSwipeLayoutRefreshing = MutableLiveData<Boolean>()

    fun getOnlyText() {
        isLoading.value = true
        noTokenIsLoading.value = true
        when (page.value) {
            0 -> {
                if (token.value != "123") {
                    FirstRepository.getAttentionList(attentionPage.value?:0).unSafeSubscribeBy { dealData(it) }
                }
                FirstRepository.getAttentionRecommend()
                    .unSafeSubscribeBy { dealRecommendUserData(it) }
            }
            1 -> {
                FirstRepository.getRecommend().unSafeSubscribeBy { dealData(it) }
            }
            2 -> {
                FirstRepository.getLatest().unSafeSubscribeBy { dealData(it) }
            }
            3 -> {
                FirstRepository.getOnlyText().unSafeSubscribeBy { dealData(it) }
            }
            4 -> {
                FirstRepository.getPicture().unSafeSubscribeBy { dealData(it) }
            }
            5->{
                FirstRepository.searchJoke(keyword.value?:"",attentionPage.value?:0).unSafeSubscribeBy { dealData(it) }
            }
            6->{
                FirstRepository.getUserJoke(userId,attentionPage.value?:1).unSafeSubscribeBy { dealData(it) }
            }
            7 ->{
                FirstRepository.getUserLikeJoke(userId,attentionPage.value?:1).unSafeSubscribeBy { dealData(it) }
            }
        }
    }


    private fun dealRecommendUserData(it: AttentionRecommendResponse) {
        for (data in it) {
            newRecommendUserData.add(data)
            oldRecommendUserData.add(data)
        }
        noTokenIsLoading.value = true
        isLoading.value = false
        //请求成功后参数变为false，因为观察了这个数据，达到一个回调的目的
        isSwipeLayoutRefreshing.value = false
    }

    private fun dealData(it: FirstTextResponse) {
        oldTextData = newTextData
        for (data in it) {
            newTextData.add(data)
        }
        isLoading.value = false
        //请求成功后参数变为false，因为观察了这个数据，达到一个回调的目的
        isSwipeLayoutRefreshing.value = false
        attentionPage.value = attentionPage.value?.plus(1)
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

    fun followUser(status: String, userId: String, block: (Boolean) -> Unit) {
        FirstRepository.followUser(status, userId)
            .unSafeSubscribeBy {
                if (it.code == 200) block(true)
            }
    }

    fun clearList(block: () -> Unit) {
        newRecommendUserData.clear()
        newTextData.clear()
        block()
        oldRecommendUserData.clear()
        oldTextData.clear()
        //下拉刷新数据时，先将此参数变为true，意味着正在请求
        isSwipeLayoutRefreshing.value = true
        getOnlyText()
    }
}