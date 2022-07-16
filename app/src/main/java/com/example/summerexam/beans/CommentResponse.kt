package com.example.summerexam.beans

data class CommentResponse(
    val comments: List<Comment>,
    val count: Int
)