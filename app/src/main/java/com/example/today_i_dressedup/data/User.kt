package com.example.today_i_dressedup.data

import java.io.Serializable

data class User(
    val uid: String,
    val nickname: String = "",
    val token: String = "",
    val post_ids: List<String> = emptyList(),
    val like_post_ids: List<String> = emptyList(),
    val dislike_post_ids: List<String> = emptyList()
) : Serializable
