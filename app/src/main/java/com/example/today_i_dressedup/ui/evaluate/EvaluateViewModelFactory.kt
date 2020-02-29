package com.example.today_i_dressedup.ui.evaluate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.today_i_dressedup.data.repository.PostRepository
import com.example.today_i_dressedup.data.repository.UserRepository

class EvaluateViewModelFactory(private val userRepository: UserRepository, private val postRepository: PostRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EvalueateViewModel(userRepository, postRepository) as T
    }
}