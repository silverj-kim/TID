package com.example.today_i_dressedup.data.repository

import com.example.today_i_dressedup.data.firebase.FirebaseSource

class PostRepository (private val firebase: FirebaseSource) {

    companion object {
        @Volatile private var instance: PostRepository? = null

        @JvmStatic fun getInstance(): PostRepository =
            instance ?: synchronized(this) {
                instance ?: PostRepository(FirebaseSource.getInstance()).also {
                    instance = it
                }
            }
    }

    fun uploadPost(filePath: String){
        firebase.uploadPost(filePath)
    }

    fun loadMyPost() = firebase.loadMyPost()

    fun loadAllPost() = firebase.loadAllPosts()

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()
}