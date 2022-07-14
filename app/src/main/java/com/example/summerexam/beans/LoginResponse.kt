package com.example.summerexam.beans

data class LoginResponse(
    val token: String,
    val type: String,
    val userInfo: UserInfo
)