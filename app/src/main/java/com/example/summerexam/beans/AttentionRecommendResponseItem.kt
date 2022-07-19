package com.example.summerexam.beans

data class AttentionRecommendResponseItem(
    val avatar: String,
    val fansNum: String,
    var isAttention: Boolean,
    val jokesNum: String,
    val nickname: String,
    val userId: Int
)