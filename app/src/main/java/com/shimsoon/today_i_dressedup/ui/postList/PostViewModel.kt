package com.shimsoon.today_i_dressedup.ui.postList

import androidx.lifecycle.ViewModel
import com.shimsoon.today_i_dressedup.data.repository.PostRepository

class PostViewModel(private val postRepository: PostRepository): ViewModel()  {
    fun loadMyPost() = postRepository.loadMyPost()

    fun loadPostILiked() = postRepository.loadPostILiked()

    fun loadPostIDisliked() = postRepository.loadPostIDisliked()

}