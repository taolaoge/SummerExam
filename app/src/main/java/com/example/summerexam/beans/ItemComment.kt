package com.example.summerexam.beans

data class ItemComment(
    val commentItemId: Int,
    val commentParentId: Int,
    val commentUser: CommentUserX,
    val commentedNickname: String,
    val commentedUserId: Int,
    val content: String,
    val isReplyChild: Boolean,
    val jokeId: Int,
    val timeStr: String
)