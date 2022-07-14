package com.example.summerexam.services

import com.example.summerexam.network.ApiGenerator

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/14
 */
interface ComprehensiveService {
    companion object {
        val INSTANCE by lazy {
            ApiGenerator.getApiService(ComprehensiveService::class)
        }
    }


}