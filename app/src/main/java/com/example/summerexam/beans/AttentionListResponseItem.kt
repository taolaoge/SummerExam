package com.example.summerexam.beans

data class AttentionListResponseItem(
    val attention: Int,
    val avatar: String,
    val nickname: String,
    val signature: String,
    val userId: Int
)