package com.example.summerexam.services

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
 * date : 2022/7/19
 */
interface SearchService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(SearchService::class)
        }
    }

    @POST("home/latest")
    @FormUrlEncoded
    fun searchJoke(@Field("keyword") keyword:String,@Field("page") page:String): Single<ApiWrapper<FirstTextResponse>>
}