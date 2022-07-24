package com.example.summerexam.services

import com.example.summerexam.beans.*
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

    /**
     * 获取指定用户发布的作品
     *
     * @param targetUserId
     * @param page
     * @return
     */
    @POST("jokes/whole_jokes/list")
    @FormUrlEncoded
    fun getUserJoke(
        @Field("targetUserId") targetUserId: String,
        @Field("page") page: Int
    ): Single<ApiWrapper<FirstTextResponse>>

    /**
     *
     *
     * @param targetUserId
     * @return
     */
    @POST("user/info/target")
    @FormUrlEncoded
    fun getTargetUserInfo(
        @Field("targetUserId") targetUserId: String,
    ):Single<ApiWrapper<TargetUserinfoResponse>>


    @POST("user/like/list")
    @FormUrlEncoded
    fun getUserLikeJoke(
        @Field("page") page: Int
    ): Single<ApiWrapper<List<FirstTextResponseItem>>>


    @POST("user/comment/list")
    @FormUrlEncoded
    fun getMineComment(
        @Field("page") page:Int)
    :Single<ApiWrapper<List<MyCommentResponseItem>>>
}