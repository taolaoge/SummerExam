package com.example.summerexam.services

import com.example.summerexam.beans.OnlyTextResponse
import com.example.summerexam.network.ApiGenerator
import com.ndhzs.lib.common.network.ApiWrapper
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
interface OnlyTextService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(OnlyTextService::class)
        }
    }

    /**
     * 获取主页的纯文列表数据
     */
    @POST("home/text")
    fun getOnlyText(): Single<ApiWrapper<OnlyTextResponse>>

    /**
     * 给段子点赞或者取消点赞
     */
    @POST("jokes/like")
    @FormUrlEncoded
    fun likeJoke(@Field("id") id: Int, @Field("status") status: Boolean): Single<ApiWrapper<Any>>

    /**
     * 给段子点踩或者取消点踩
     */
    @POST("jokes/unlike")
    @FormUrlEncoded
    fun dislikeJoke(@Field("id") id: Int, @Field("status") status: Boolean):Single<ApiWrapper<Any>>
}