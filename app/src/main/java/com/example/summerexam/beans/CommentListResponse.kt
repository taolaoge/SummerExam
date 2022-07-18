package com.example.summerexam.beans

data class CommentListResponse(
    val comments: List<Comment>,
    val count: Int
)