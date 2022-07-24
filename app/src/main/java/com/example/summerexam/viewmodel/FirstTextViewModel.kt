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

    //关注fragment中推荐关注的用户列表
    private val _newRecommendUserData =
        MutableLiveData<List<AttentionRecommendResponseItem>>()


    val newRecommendUserData: LiveData<List<AttentionRecommendResponseItem>>
        get() = _newRecommendUserData
    //新的数据集合，差分刷新使用
    private val _newTextData =
        MutableLiveData<List<FirstTextResponseItem>>()


    val newTextData: LiveData<List<FirstTextResponseItem>>
        get() = _newTextData

    private val _needRefresh = MutableLiveData<Boolean>()
    val needRefresh: LiveData<Boolean>
        get() = _needRefresh

    var freshPosition = 0

    private val _needRefreshRecommend = MutableLiveData<Boolean>()
    val needRecommend: LiveData<Boolean>
        get() = _needRefreshRecommend

    private val _page = MutableLiveData<Int>()
    val page: LiveData<Int>
        get() = _page

    /**
     * 判断是否正在请求数据
     * 因为我是用的监听最后一个item来继续请求更多数据，如果没有这个参数，很有可能重复请求两次数据
     */
    var isLoading = false

    private fun getRecommendList() {
        FirstRepository.getAttentionRecommend()
            .safeSubscribeBy { dealRecommendUserData(it) }
    }

    fun getOnlyText() {
        isLoading = true
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
                FirstRepository.getUserLikeJoke(attentionPage.value ?: 1)
                    .safeSubscribeBy { dealData(it) }
            }
        }
    }

    private fun dealRecommendUserData(it: List<AttentionRecommendResponseItem>) {
        _newRecommendUserData.value = buildList {
            addAll(it)
        }
        isLoading = false
        //请求成功后参数变为false，因为观察了这个数据，达到一个回调的目的
    }

    private fun dealData(it: List<FirstTextResponseItem>) {
        _newTextData.value = buildList {
            addAll(newTextData.value ?: emptyList())
            addAll(it)
        }
        isLoading = false
        //请求成功后参数变为false，因为观察了这个数据，达到一个回调的目的
        attentionPage.value = attentionPage.value?.plus(1)
    }

    fun likeJoke(id: Int, status: Boolean, position: Int) {
        FirstRepository.likeJoke(id, status)
            .safeSubscribeBy {
                if (it.code == 200) {
                    if (!status) {
                        _newTextData.value?.get(position)?.info?.likeNum =
                            _newTextData.value?.get(position)?.info?.likeNum?.minus(1) ?: 0
                        _newTextData.value?.get(position)?.info?.isLike = status
                    } else {
                        _newTextData.value?.get(position)?.info?.likeNum =
                            _newTextData.value?.get(position)?.info?.likeNum?.plus(1) ?: 0
                        _newTextData.value?.get(position)?.info?.isLike = status
                    }
                    toast("操作成功")
                    freshPosition = position
                    _needRefresh.value = true
                }
            }
    }

    fun dislikeJoke(id: Int, status: Boolean, position: Int) {
        FirstRepository.dislikeJoke(id, status)
            .safeSubscribeBy {
                if (it.code == 200) {
                    if (!status) {
                        _newTextData.value?.get(position)?.info?.disLikeNum =
                            _newTextData.value?.get(position)?.info?.disLikeNum?.minus(1) ?: 0
                        _newTextData.value?.get(position)?.info?.isUnlike = status
                    } else {
                        _newTextData.value?.get(position)?.info?.disLikeNum =
                            _newTextData.value?.get(position)?.info?.disLikeNum?.plus(1) ?: 0
                        _newTextData.value?.get(position)?.info?.isUnlike = status
                    }
                    toast("操作成功")
                    freshPosition = position
                    _needRefresh.value = true
                }
            }
    }

    /**
     * @param type 判断rv的类型
     */
    fun followUser(status: String, userId: String, position: Int, type: Int) {
        FirstRepository.followUser(status, userId)
            .safeSubscribeBy {
                if (it.code == 200) {
                    if (type == 1) {
                        _newTextData.value?.get(position)?.info?.isAttention = status == "1"
                        freshPosition = position
                        _needRefresh.value = true
                    } else {
                        _newRecommendUserData.value?.get(position)?.isAttention = status == "1"
                        freshPosition = position
                        _needRefreshRecommend.value = true
                    }
                    toast("操作成功")
                }
            }
    }

    fun freshList() {
        FirstRepository.getOnlyText().safeSubscribeBy {
            _newTextData.value = buildList {
                addAll(it)
            }
        }
    }

    fun clearAttentionList() {
        _newTextData.value = emptyList()
    }

    fun freshRecommendList() {
        FirstRepository.getAttentionRecommend().safeSubscribeBy {
            _newRecommendUserData.value = buildList {
                addAll(it)
            }
        }
    }

    fun clearRecommendList(){
        _newRecommendUserData.value = emptyList()
    }

    fun putKeyword(keyword: String) {
        _keyword.value = keyword
    }

    fun putPage(page: Int) {
        _page.value = page
    }

    fun changeNeedRefresh() {
        _needRefresh.value = false
    }

    fun changeNeedRefreshRecommend(){
        _needRefreshRecommend.value = false
    }
}