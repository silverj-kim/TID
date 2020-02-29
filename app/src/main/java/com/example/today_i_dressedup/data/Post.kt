package com.example.today_i_dressedup.data

data class Post(
    val userId: String = "",
    val imgUrl: String = "",
    val numOfLike: Long = 0,
    val numOfDislike: Long = 0,
    val timeStamp: Long = 0
)