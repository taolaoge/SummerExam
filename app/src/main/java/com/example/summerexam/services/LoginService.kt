package com.example.summerexam.services

import com.example.summerexam.beans.LoginResponse
import com.example.summerexam.network.ApiGenerator
import com.ndhzs.lib.common.network.ApiWrapper
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
interface LoginService {

    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(LoginService::class)
        }
    }

    /**
     * 验证码登陆
     */
    @FormUrlEncoded
    @POST("user/login/code")
    fun login(@Field("code") code:String, @Field("phone") phone:String):Single<ApiWrapper<LoginResponse>>


    /**
     * 获取手机登陆的验证码
     */
    @FormUrlEncoded
    @POST("user/login/get_code")
    fun getCode(@Field("phone") phone:String):Single<ApiWrapper<Any>>

}