package com.example.summerexam.beans

data class CommentResponse(
    val commentId: Int,
    val commentUser: CommentUserXX,
    val content: String,
    val isLike: Boolean,
    val itemCommentList: List<ItemCommentX>,
    val itemCommentNum: Int,
    val jokeId: Int,
    val jokeOwnerUserId: Int,
    val likeNum: Int,
    val timeStr: String
)