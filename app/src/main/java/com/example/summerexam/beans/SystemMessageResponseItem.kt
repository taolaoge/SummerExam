package com.example.summerexam.beans

data class SystemMessageResponseItem(
    val content: String,
    val id: Int,
    val isRead: Boolean,
    val targetId: Int,
    val timeStr: String,
    val title: String,
    val type: Int
)