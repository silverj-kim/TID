package com.example.today_i_dressedup.data

import java.io.Serializable
import java.util.*

data class Post(
    var id: String = "",
    val userId: String = "",
    val imgUrl: String = "",
    val numOfLike: Long = 0,
    val numOfDislike: Long = 0,
    val timeStamp: Date? = null,
    val voters: List<String> = emptyList()
) : Serializable