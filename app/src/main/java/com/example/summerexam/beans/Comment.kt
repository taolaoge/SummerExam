package com.example.summerexam.beans

data class Comment(
    val commentId: Int,
    val commentUser: CommentUser,
    val content: String,
    var isLike: Boolean,
    val itemCommentList: List<ItemComment>,
    val itemCommentNum: Int,
    val jokeId: Int,
    val jokeOwnerUserId: Int,
    var likeNum: Int,
    val timeStr: String
)