package com.example.summerexam.beans

data class ItemCommentX(
    val commentItemId: Int,
    val commentParentId: Int,
    val commentUser: CommentUserXXX,
    val commentedNickname: String,
    val commentedUserId: Int,
    val content: String,
    val isReplyChild: Boolean,
    val jokeId: Int,
    val timeStr: String
)