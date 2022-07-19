package com.ndhzs.lib.common.network

import com.example.summerexam.network.ApiException
import com.google.gson.annotations.SerializedName
import com.example.summerexam.extensions.appContext
import com.example.summerexam.extensions.edit
import com.example.summerexam.extensions.getSp
import com.example.summerexam.extensions.toast
import java.io.Serializable
import kotlin.jvm.Throws

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/29 23:06
 */
open class ApiWrapper<T>(
    @SerializedName("data")
    val data: T,
) : Serializable, ApiStatus()

open class ApiStatus(
    @SerializedName("code")
    val code: Int = 200,
    @SerializedName("msg")
    val msg: String = ""
) : Serializable {

    fun isSuccess(): Boolean {
        if (code == 0 || code == 201) toast(msg)
        if (code == 202) {
            toast("用户登陆状态过期，请重新登陆")
            //清除储存的token
            appContext.getSp("token").edit {
                clear()
            }
        }
        return code == 200 || code == 0 || code == 201 || code == 202
    }

    @Throws(ApiException::class)
    fun throwApiExceptionIfFail() {
        if (!isSuccess()) throw ApiException(code, msg)
    }
}

