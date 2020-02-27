package com.example.today_i_dressedup.ui.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.today_i_dressedup.data.repository.PostRepository
import com.example.today_i_dressedup.data.repository.UserRepository
import com.example.today_i_dressedup.ui.evaluate.EvalueateViewModel

class MyPageViewModelFactory(private val repository: PostRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyPageViewModel(repository) as T
    }
}