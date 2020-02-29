package com.example.today_i_dressedup.ui.evaluate

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.today_i_dressedup.data.repository.PostRepository
import com.example.today_i_dressedup.data.repository.UserRepository
import com.example.today_i_dressedup.util.startLoginActivity

class EvalueateViewModel(private val userRepository: UserRepository, private val postRepository: PostRepository) : ViewModel() {

    val user by lazy {
        userRepository.currentUser()
    }

    fun logout(view: View) {
        userRepository.logout()
        view.context.startLoginActivity()
    }

    fun loadAllPosts() = postRepository.loadAllPost()
}