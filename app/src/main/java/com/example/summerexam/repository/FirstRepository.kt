package com.example.summerexam.repository

import com.example.summerexam.beans.AttentionRecommendResponse
import com.example.summerexam.beans.AttentionRecommendResponseItem
import com.example.summerexam.beans.FirstTextResponse
import com.example.summerexam.beans.FirstTextResponseItem
import com.example.summerexam.services.FirstTextService
import com.example.summerexam.extensions.mapOrThrowApiException
import com.example.summerexam.extensions.throwApiExceptionIfFail
import com.example.summerexam.extensions.unSafeSubscribeBy
import com.example.summerexam.services.UserinfoService
import com.ndhzs.lib.common.network.ApiWrapper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/16
 */
object FirstRepository {
    fun getOnlyText(): Single<FirstTextResponse> {
        return FirstTextService.INSTANCE.getOnlyText()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun likeJoke(id: Int, status: Boolean): Single<ApiWrapper<Any>> {
        return FirstTextService.INSTANCE.likeJoke(id, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
    }

    fun dislikeJoke(id: Int, status: Boolean): Single<ApiWrapper<Any>>  {
        return FirstTextService.INSTANCE.dislikeJoke(id, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
    }

    fun followUser(status: String, userId: String): Single<ApiWrapper<Any>>  {
        return FirstTextService.INSTANCE.followUser(status, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
    }

    fun getPicture():Single<List<FirstTextResponseItem>>{
        return FirstTextService.INSTANCE.getPicture()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun getRecommend():Single<List<FirstTextResponseItem>>{
        return FirstTextService.INSTANCE.getRecommend()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun getLatest():Single<List<FirstTextResponseItem>>{
        return FirstTextService.INSTANCE.getLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun getAttentionList(page:Int):Single<FirstTextResponse>{
        return FirstTextService.INSTANCE.getAttentionList(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun getAttentionRecommend():Single<List<AttentionRecommendResponseItem>>{
        return FirstTextService.INSTANCE.getAttentionRecommend()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun searchJoke(keyword:String,page:Int):Single<List<FirstTextResponseItem>>{
        return FirstTextService.INSTANCE.searchJoke(keyword,page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun getUserJoke(userId: String,page:Int): Single<FirstTextResponse> {
        return UserinfoService.INSTANCE.getUserJoke(userId, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun getUserLikeJoke(page:Int): Single<List<FirstTextResponseItem>> {
        return UserinfoService.INSTANCE.getUserLikeJoke(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }
}