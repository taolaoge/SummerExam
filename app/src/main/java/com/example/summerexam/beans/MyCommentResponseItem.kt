package com.example.summerexam.beans

data class MyCommentResponseItem(
    val commentId: Int,
    val content: String,
    val extraContent: String,
    val msgId: Int,
    val msgItemType: Int,
    val msgItemTypeDesc: String,
    val msgMainType: Int,
    val msgMainTypeDesc: String,
    val msgStatus: Int,
    val msgTime: String,
    val ownerUserId: Int,
    val targetId: Int,
    val targetNickname: String,
    val targetUserAvatar: String,
    val targetUserId: Int
)