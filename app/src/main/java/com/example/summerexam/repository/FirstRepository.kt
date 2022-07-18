package com.example.summerexam.repository

import com.example.summerexam.beans.CommentResponse
import com.example.summerexam.beans.OnlyTextResponse
import com.example.summerexam.beans.OnlyTextResponseItem
import com.example.summerexam.services.OnlyTextService
import com.ndhzs.lib.common.extensions.mapOrThrowApiException
import com.ndhzs.lib.common.extensions.throwApiExceptionIfFail
import com.ndhzs.lib.common.extensions.toast
import com.ndhzs.lib.common.extensions.unSafeSubscribeBy
import com.ndhzs.lib.common.network.ApiWrapper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/16
 */
object FirstRepository {
    fun getOnlyText(): Single<OnlyTextResponse> {
        return OnlyTextService.INSTANCE.getOnlyText()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun likeJoke(id: Int, status: Boolean): Single<ApiWrapper<Any>> {
        return OnlyTextService.INSTANCE.likeJoke(id, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
    }

    fun dislikeJoke(id: Int, status: Boolean): Single<ApiWrapper<Any>>  {
        return OnlyTextService.INSTANCE.dislikeJoke(id, status)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
    }

    fun followUser(status: String, userId: String): Single<ApiWrapper<Any>>  {
        return OnlyTextService.INSTANCE.followUser(status, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .throwApiExceptionIfFail()
    }

    fun getPicture():Single<OnlyTextResponse>{
        return OnlyTextService.INSTANCE.getPicture()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun getRecommend():Single<OnlyTextResponse>{
        return OnlyTextService.INSTANCE.getRecommend()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

    fun getLatest():Single<OnlyTextResponse>{
        return OnlyTextService.INSTANCE.getLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .mapOrThrowApiException()
    }

}