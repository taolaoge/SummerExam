package com.example.summerexam.beans

data class AttentionRecommendResponseItem(
    val avatar: String,
    var fansNum: Int,
    var isAttention: Boolean,
    val jokesNum: String,
    val nickname: String,
    val userId: Int
)