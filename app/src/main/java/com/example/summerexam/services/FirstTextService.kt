package com.example.summerexam.services

import com.example.summerexam.beans.AttentionRecommendResponse
import com.example.summerexam.beans.FirstTextResponse
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
interface FirstTextService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(FirstTextService::class)
        }
    }

    /**
     * 获取主页的纯文列表数据
     */
    @POST("home/text")
    fun getOnlyText(): Single<ApiWrapper<FirstTextResponse>>

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
    fun dislikeJoke(@Field("id") id: Int, @Field("status") status: Boolean): Single<ApiWrapper<Any>>

    /**
     * TODO
     *
     * @param status
     * @param userId
     * @return
     */
    @POST("user/attention")
    @FormUrlEncoded
    fun followUser(
        @Field("status") status: String,
        @Field("userId") userId: String
    ): Single<ApiWrapper<Any>>

    /**
     * 获取首页趣图数据
     */
    @POST("home/pic")
    fun getPicture(): Single<ApiWrapper<FirstTextResponse>>

    /**
     * 获取首页最新列表数据
     */
    @POST("home/latest")
    fun getLatest(): Single<ApiWrapper<FirstTextResponse>>

    /**
     * 获取推荐列表的数据
     */
    @POST("home/recommend")
    fun getRecommend(): Single<ApiWrapper<FirstTextResponse>>

    /**
     *获取关注的用户发布的段子列表
     */
    @POST("home/attention/list")
    @FormUrlEncoded
    fun getAttentionList(@Field("page") page: Int): Single<ApiWrapper<FirstTextResponse>>

    /**
     * 获取主页的推荐关注数据
     */
    @POST("home/attention/recommend")
    fun getAttentionRecommend(): Single<ApiWrapper<AttentionRecommendResponse>>

    /**
     * 搜索段子的方法
     * @param keyword
     * @param page
     * @return
     */
    @POST("home/latest")
    @FormUrlEncoded
    fun searchJoke(@Field("keyword") keyword:String,@Field("page") page:Int): Single<ApiWrapper<FirstTextResponse>>
}