package com.example.summerexam.services

import com.example.summerexam.beans.UserInfo
import com.example.summerexam.beans.UserInfoResponse
import com.example.summerexam.network.ApiGenerator
import com.ndhzs.lib.common.network.ApiWrapper
import io.reactivex.rxjava3.core.Single
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
interface MineService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(MineService::class)
        }
    }


    @POST("user/info")
    fun getUserInfo():Single<ApiWrapper<UserInfoResponse>>

}