package com.example.today_i_dressedup.ui.myPage

import androidx.lifecycle.ViewModel
import com.example.today_i_dressedup.data.Post
import com.example.today_i_dressedup.data.firebase.FirebaseSource
import com.example.today_i_dressedup.data.repository.PostRepository

class MyPageViewModel(private val postRepository: PostRepository): ViewModel() {

    fun uploadPostToServer(filePath: String){
        postRepository.uploadPost(filePath)
    }

    fun getPostsILiked(myId: Long){

    }

    fun getPostsIDisliked(myId: Long){

    }

    fun getMyPosts(myId: Long){

    }
}