package com.example.summerexam.beans

data class Comment(
    val commentId: Int,
    val commentUser: CommentUser,
    val content: String,
    val isLike: Boolean,
    val itemCommentList: List<ItemComment>,
    val itemCommentNum: Int,
    val jokeId: Int,
    val jokeOwnerUserId: Int,
    val likeNum: Int,
    val timeStr: String
)