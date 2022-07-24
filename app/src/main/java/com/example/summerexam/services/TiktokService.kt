package com.example.summerexam.services

import com.example.summerexam.beans.TiktokResponseItem
import com.example.summerexam.network.ApiGenerator
import com.ndhzs.lib.common.network.ApiWrapper
import io.reactivex.rxjava3.core.Single
import retrofit2.http.POST

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/24
 */
interface TiktokService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(TiktokService::class)
        }
    }

    @POST("douyin/list")
    fun getTiktokList(): Single<ApiWrapper<List<TiktokResponseItem>>>
}