package com.example.today_i_dressedup.data.firebase

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable

class FirebaseSource {

    companion object {
        @Volatile private var instance: FirebaseSource? = null

        @JvmStatic fun getInstance(): FirebaseSource =
            instance ?: synchronized(this) {
                instance ?: FirebaseSource().also {
                    instance = it
                }
            }
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }


    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun register(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

}