package com.ndhzs.lib.common.network

import com.example.summerexam.network.ApiException
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.jvm.Throws

/**
 * ...
 * @author 985892345 (Guo Xiangrui)
 * @email 2767465918@qq.com
 * @date 2022/5/29 23:06
 */
open class ApiWrapper<T> (
  @SerializedName("data")
  val data: T,
) : Serializable, ApiStatus()

open class ApiStatus(
  @SerializedName("code")
  val code: Int = 0,
  @SerializedName("msg")
  val msg: String = ""
) : Serializable {

  fun isSuccess(): Boolean {
    return code == 0
  }

  @Throws(ApiException::class)
  fun throwApiExceptionIfFail() {
  if (!isSuccess()) throw ApiException(code, msg)
}
}

