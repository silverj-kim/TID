package com.example.today_i_dressedup.data

import com.google.firebase.database.Exclude

data class User(
    val uid: String,
    val name: String,
    val email: String,
    @Exclude
    val isAuthenticated: Boolean,
    @Exclude
    val isNew: Boolean,
    @Exclude
    val isCreated: Boolean
)