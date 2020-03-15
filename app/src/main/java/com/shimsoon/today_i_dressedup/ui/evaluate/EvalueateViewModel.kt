package com.shimsoon.today_i_dressedup.ui.evaluate

import android.view.View
import androidx.lifecycle.ViewModel
import com.shimsoon.today_i_dressedup.data.repository.PostRepository
import com.shimsoon.today_i_dressedup.data.repository.UserRepository
import com.shimsoon.today_i_dressedup.util.startLoginActivity

class EvalueateViewModel(private val userRepository: UserRepository, private val postRepository: PostRepository) : ViewModel() {

    val user by lazy {
        userRepository.currentUser()
    }

    fun logout(view: View) {
        userRepository.logout()
        view.context.startLoginActivity()
    }

    fun loadAllPosts() = postRepository.loadAllPost()

    fun likePost(postId: String){
        postRepository.likePost(postId)
    }

    fun dislikePost(postId: String){
        postRepository.dislikePost(postId)
    }

    fun updateUserToken(){
        userRepository.updateUserToken()
    }

    fun sendNotification(userId: String, postId: String){
        postRepository.sendNotification(userId, postId)
    }
}