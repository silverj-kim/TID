package com.shimsoon.today_i_dressedup.data.repository

import com.shimsoon.today_i_dressedup.data.firebase.FirebaseSource
import java.io.File

class PostRepository(private val firebase: FirebaseSource) {

    companion object {
        @Volatile
        private var instance: PostRepository? = null

        @JvmStatic
        fun getInstance(): PostRepository =
            instance ?: synchronized(this) {
                instance ?: PostRepository(FirebaseSource.getInstance()).also {
                    instance = it
                }
            }
    }

    fun uploadPost(file: File) {
        firebase.uploadPost(file)
    }


    fun loadMyPost() = firebase.loadMyPost()

    fun loadPostLiked() = firebase.loadPostILiked()

    fun loadPostDisliked() = firebase.loadPostIDisliked()

    fun loadAllPost() = firebase.loadAllPosts()

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    fun dislikePost(postId: String) {
        firebase.dislikePost(postId)
    }

    fun likePost(postId: String) {
        firebase.likePost(postId)
    }

    fun getLiveUploadStatus() = firebase.liveUploadState

    fun sendNotification(userId: String, postId: String) {
        firebase.sendNotification(userId, postId)
    }
}