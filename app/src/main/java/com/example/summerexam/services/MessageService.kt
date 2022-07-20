package com.example.summerexam.services

import com.example.summerexam.beans.SystemMessageResponse
import com.example.summerexam.beans.UserMessageResponse
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
interface MessageService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(MessageService::class)
        }
    }

    /**
     *获取系统消息列表
     */
    @POST("user/message/system")
    @FormUrlEncoded
    fun getMessageSystem(@Field("page") page: Int): Single<ApiWrapper<SystemMessageResponse>>

    /**
     * 获取指定用户的消息列表
     *
     * @param page 分页
     * @param type type=0 赞 type=1 评论 type=2 关注
     * @return
     */
    @POST("user/message/list")
    @FormUrlEncoded
    fun getUserMessage(
        @Field("page") page: Int,
        @Field("type") type: Int
    ): Single<ApiWrapper<UserMessageResponse>>
}