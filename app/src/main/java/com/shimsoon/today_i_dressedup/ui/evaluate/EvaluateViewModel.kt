package com.shimsoon.today_i_dressedup.ui.evaluate

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shimsoon.today_i_dressedup.data.Post
import com.shimsoon.today_i_dressedup.data.Status
import com.shimsoon.today_i_dressedup.data.repository.PostRepository
import com.shimsoon.today_i_dressedup.data.repository.UserRepository
import com.shimsoon.today_i_dressedup.util.startLoginActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList

class EvaluateViewModel : ViewModel() {

    private val userRepository: UserRepository = UserRepository.getInstance()
    private val postRepository: PostRepository = PostRepository.getInstance()

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    val statusLiveData = MutableLiveData<Status>()
    val posts = MutableLiveData<List<Post>>()

    val user by lazy {
        userRepository.currentUser()
    }

    fun logout(view: View) {
        userRepository.logout()
        view.context.startLoginActivity()
    }

    fun loadAllPosts(){
        statusLiveData.value = Status.LOADING
        val disposable = postRepository
            .loadAllPost()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                statusLiveData.value = Status.SUCCESS
                posts.value = it
            }, {
                statusLiveData.value = Status.FAILURE
            })

        disposables.add(disposable)
    }

    fun likePost(postId: String) {
        postRepository.likePost(postId)
    }

    fun dislikePost(postId: String) {
        postRepository.dislikePost(postId)
    }

    fun updateUserToken() {
        userRepository.updateUserToken()
    }

    fun sendNotification(userId: String, postId: String) {
        postRepository.sendNotification(userId, postId)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}