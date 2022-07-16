package com.example.summerexam.services

import com.example.summerexam.beans.CommentResponse
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
 * date : 2022/7/16
 */
interface CommentService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(CommentService::class)
        }
    }

    /**
     * 获取评论列表
     */
    @POST("jokes/comment/list")
    @FormUrlEncoded
    fun getCommentList(@Field("jokeId")id:Int):Single<ApiWrapper<CommentResponse>>

    /**
     * 给指定的评论点赞，需要传入评论的id，status，点赞或者取消点赞
     */
    @POST("jokes/comment/like")
    @FormUrlEncoded
    fun likeComment(@Field("commentId") commentId:String,@Field("status")status:Boolean):Single<ApiWrapper<Any>>
}