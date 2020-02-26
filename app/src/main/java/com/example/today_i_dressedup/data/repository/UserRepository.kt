package com.example.today_i_dressedup.data.repository

import com.example.today_i_dressedup.data.firebase.FirebaseSource

class UserRepository (private val firebase: FirebaseSource) {

    companion object {
        @Volatile private var instance: UserRepository? = null

        @JvmStatic fun getInstance(): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(FirebaseSource.getInstance()).also {
                    instance = it
                }
            }
    }

    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String) = firebase.register(email, password)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()
}