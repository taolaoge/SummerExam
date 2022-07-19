package com.example.summerexam.services

import com.example.summerexam.beans.AttentionRecommendResponse
import com.example.summerexam.beans.CommentResponse
import com.example.summerexam.beans.OnlyTextResponse
import com.example.summerexam.network.ApiGenerator
import com.ndhzs.lib.common.network.ApiWrapper
import io.reactivex.rxjava3.core.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

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

    /**
     * 关注发段子的人
     */
    @POST("user/attention")
    @FormUrlEncoded
    fun followUser(@Field("status") status:String,@Field("userId") userId:String):Single<ApiWrapper<Any>>

    /**
     * 获取首页趣图数据
     */
    @POST("home/pic")
    fun getPicture():Single<ApiWrapper<OnlyTextResponse>>

    /**
     * 获取首页最新列表数据
     */
    @POST("home/latest")
    fun getLatest():Single<ApiWrapper<OnlyTextResponse>>

    /**
     * 获取推荐列表的数据
     */
    @POST("home/recommend")
    fun getRecommend():Single<ApiWrapper<OnlyTextResponse>>

    /**
     *获取关注的用户发布的段子列表
     */
    @POST("home/attention/list")
    @FormUrlEncoded
    fun getAttentionList(@Field("page") page:Int):Single<ApiWrapper<OnlyTextResponse>>

    /**
     * 获取主页的推荐关注数据
     */
    @POST("home/attention/recommend")
    fun getAttentionRecommend():Single<ApiWrapper<AttentionRecommendResponse>>
}