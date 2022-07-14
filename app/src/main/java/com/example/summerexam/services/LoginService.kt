package com.example.summerexam.services

import com.example.summerexam.beans.LoginResponse
import com.example.summerexam.network.ApiGenerator
import com.example.summerexam.network.PROJECT_TOKEN
import com.ndhzs.lib.common.network.ApiWrapper
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

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

    @Headers("project_token:$PROJECT_TOKEN")
    @GET("/user/login/code")
    fun login(@Query("code") code:String,@Query("phone") phone:String):Single<ApiWrapper<LoginResponse>>

}