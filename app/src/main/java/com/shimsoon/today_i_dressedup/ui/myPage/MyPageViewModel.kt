package com.shimsoon.today_i_dressedup.ui.myPage

import androidx.lifecycle.ViewModel
import com.shimsoon.today_i_dressedup.data.repository.PostRepository

class MyPageViewModel : ViewModel() {

    private val postRepository: PostRepository = PostRepository.getInstance()

    fun uploadPostToServer(filePath: String) {
        postRepository.uploadPost(filePath)
    }

    fun loadPostsILiked() {

    }

    fun loadPostsIDisliked() {

    }

    fun loadMyPosts() {
        postRepository.loadMyPost()
    }

    fun getUploadLiveStatus() = postRepository.getLiveUploadStatus()
}