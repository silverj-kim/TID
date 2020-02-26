package com.example.today_i_dressedup.ui.evaluate

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.today_i_dressedup.data.repository.UserRepository
import com.example.today_i_dressedup.util.startLoginActivity

class EvalueateViewModel(private val repository: UserRepository) : ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    fun logout(view: View){
        repository.logout()
        view.context.startLoginActivity()
    }
}