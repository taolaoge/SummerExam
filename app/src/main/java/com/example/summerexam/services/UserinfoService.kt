package com.example.summerexam.services

import com.example.summerexam.beans.FirstTextResponse
import com.example.summerexam.beans.TargetUserinfoResponse
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
 * date : 2022/7/20
 */
interface UserinfoService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(UserinfoService::class)
        }
    }

    @POST("jokes/whole_jokes/list")
    @FormUrlEncoded
    fun getUserJoke(
        @Field("targetUserId") targetUserId: String,
        @Field("page") page: Int
    ): Single<ApiWrapper<FirstTextResponse>>

    @POST("user/info/target")
    @FormUrlEncoded
    fun getTargetUserInfo(
        @Field("targetUserId") targetUserId: String,
    ):Single<ApiWrapper<TargetUserinfoResponse>>

    @POST("jokes/whole_jokes/like/list")
    @FormUrlEncoded
    fun getUserLikeJoke(
        @Field("targetUserId") targetUserId: String,
        @Field("page") page: Int
    ): Single<ApiWrapper<FirstTextResponse>>
}