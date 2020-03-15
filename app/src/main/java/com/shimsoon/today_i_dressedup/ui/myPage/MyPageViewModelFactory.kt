package com.shimsoon.today_i_dressedup.ui.myPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shimsoon.today_i_dressedup.data.repository.PostRepository

class MyPageViewModelFactory(private val repository: PostRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyPageViewModel(repository) as T
    }
}