package com.example.summerexam.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.summerexam.baseui.BaseViewModel
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
class FirstTextViewModel : BaseViewModel() {
    val token = MutableLiveData(appContext.getSp("token").getString("token", "123"))

    private val attentionPage = MutableLiveData(1)

    private val _keyword = MutableLiveData<String>()
    val keyword: LiveData<String>
        get() = _keyword

    var userId = ""

    private val _code = MutableLiveData<Int>()
    val code: LiveData<Int>
        get() = _code

    val noTokenIsLoading = MutableLiveData(false)

    //关注fragment中推荐关注的用户列表
    private val _newRecommendUserData =
        MutableLiveData<List<AttentionRecommendResponseItem>>()
    val newRecommendUserData: LiveData<List<AttentionRecommendResponseItem>>
        get() = _newRecommendUserData

    private val _oldRecommendUserData =
        MutableLiveData<List<AttentionRecommendResponseItem>>()
    val oldRecommendUserData: LiveData<List<AttentionRecommendResponseItem>>
        get() = _newRecommendUserData

    //新的数据集合，差分刷新使用
    private val _newTextData =
        MutableLiveData<List<FirstTextResponseItem>>()
    val newTextData: LiveData<List<FirstTextResponseItem>>
        get() = _newTextData

    val oldTextData = ArrayList<FirstTextResponseItem>()

    val testList = ArrayList<FirstTextResponseItem>()

    val needRefresh = MutableLiveData<Boolean>()

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int>
        get() = _page

    /**
     * 判断是否正在请求数据
     * 因为我是用的监听最后一个item来继续请求更多数据，如果没有这个参数，很有可能重复请求两次数据
     */
    var isLoading = false

    /**
     * 监听 判断是否请求数据完成，如果请求数据已完成，此参数变为false
     * 监听此参数的就会被回调，取消SwipeLayout的动画
     * 其实接口回调和高阶函数更好，但是这里没有page参数，不好判断是否回调
     */
    val isSwipeLayoutRefreshing = MutableLiveData<Boolean>()

    private fun getRecommendList() {
        FirstRepository.getAttentionRecommend()
            .safeSubscribeBy { dealRecommendUserData(it) }
    }

    fun getOnlyText() {
        isLoading = true
        noTokenIsLoading.value = true
        when (page.value) {
            0 -> {
                if (token.value != "123") {
                    FirstRepository.getAttentionList(attentionPage.value ?: 0)
                        .safeSubscribeBy { dealData(it) }
                }
                getRecommendList()
            }
            1 -> {
                FirstRepository.getRecommend().safeSubscribeBy { dealData(it) }
            }
            2 -> {
                FirstRepository.getLatest().safeSubscribeBy { dealData(it) }
            }
            3 -> {
                FirstRepository.getOnlyText().safeSubscribeBy { dealData(it) }
            }
            4 -> {
                FirstRepository.getPicture().safeSubscribeBy { dealData(it) }
            }
            5 -> {
                FirstRepository.searchJoke(keyword.value ?: "", attentionPage.value ?: 0)
                    .safeSubscribeBy { dealData(it) }
            }
            6 -> {
                FirstRepository.getUserJoke(userId, attentionPage.value ?: 1)
                    .safeSubscribeBy { dealData(it) }
            }
            7 -> {
                FirstRepository.getUserLikeJoke(userId, attentionPage.value ?: 1)
                    .safeSubscribeBy { dealData(it) }
            }
        }
    }


    private fun dealRecommendUserData(it: List<AttentionRecommendResponseItem>) {
        _newRecommendUserData.value = buildList {
            addAll(it)
        }
        noTokenIsLoading.value = true
        isLoading = false
        //请求成功后参数变为false，因为观察了这个数据，达到一个回调的目的
    }

    private fun dealData(it: List<FirstTextResponseItem>) {
        _newTextData.value = buildList {
            addAll(newTextData.value ?: emptyList())
            addAll(it)
        }
        for (i in it) oldTextData.add(i)
        for (i in it) testList.add(i)
        isLoading = false
        //请求成功后参数变为false，因为观察了这个数据，达到一个回调的目的
        attentionPage.value = attentionPage.value?.plus(1)
    }

    fun likeJoke(id: Int, status: Boolean, position: Int) {
        FirstRepository.likeJoke(id, status)
            .safeSubscribeBy {
                _code.value = it.code
            }
    }

    fun dislikeJoke(id: Int, status: Boolean, position: Int) {
        FirstRepository.dislikeJoke(id, status)
            .safeSubscribeBy {
                _code.value = it.code
            }
    }

    /**
     * @param type 判断rv的类型
     */
    fun followUser(status: String, userId: String, position: Int, type: Int) {
        FirstRepository.followUser(status, userId)
            .safeSubscribeBy {
                if (type == 1) {
                   oldTextData[position].info.isAttention = status == "1"
                   testList[position].info.isAttention = status != "1"
                    _newTextData.value = buildList {
                        addAll(oldTextData)
                    }
                    needRefresh.value = true
                } else {
                    _oldRecommendUserData.value?.get(position)?.isAttention = status == "1"
                }
                _code.value = it.code
            }
    }

    fun clearList() {
        FirstRepository.getOnlyText().safeSubscribeBy {
            _newTextData.value = buildList {
                addAll(it)
            }
        }
    }

    fun clearRecommendList() {
        FirstRepository.getAttentionRecommend().safeSubscribeBy {
            _newRecommendUserData.value = buildList {
                addAll(it)
            }
        }
    }

    fun putKeyword(keyword: String) {
        _keyword.value = keyword
    }

    fun putPage(page: Int) {
        _page.value = page
    }
}