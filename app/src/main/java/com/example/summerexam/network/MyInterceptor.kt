package com.example.summerexam.network

import com.ndhzs.lib.common.extensions.appContext
import com.ndhzs.lib.common.extensions.getSp
import okhttp3.Interceptor
import okhttp3.Response

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/7/15
 */
class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("project_token", PROJECT_TOKEN)
            .addHeader("token", appContext.getSp("token").getString("token","123")!!)
            .build()
        return chain.proceed(newRequest)
    }
}