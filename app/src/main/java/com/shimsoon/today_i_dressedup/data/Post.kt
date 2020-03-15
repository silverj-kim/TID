package com.shimsoon.today_i_dressedup.data

import java.io.Serializable
import java.util.*

data class Post(
    var id: String = "",
    var userId: String = "",
    var imgUrl: String = "",
    var numOfLike: Long = 0,
    var numOfDislike: Long = 0,
    var timeStamp: Date? = null,
    var voters: List<String> = emptyList()
) : Serializable
