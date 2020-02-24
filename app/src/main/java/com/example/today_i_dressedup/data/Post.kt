package com.example.today_i_dressedup.data

data class Post(
    val id: Long,
    val userId: Long,
    val imgUrl: String,
    val numOfLike: Long,
    val numOfDislike: Long,
    val timeStamp: Long
)