package com.example.today_i_dressedup.ui.postList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.today_i_dressedup.data.repository.PostRepository

class PostViewModelFactory(private val repository: PostRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PostViewModel(repository) as T
    }
}