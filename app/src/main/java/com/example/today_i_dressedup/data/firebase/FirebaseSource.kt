package com.example.today_i_dressedup.data.firebase

import android.net.Uri
import android.util.Log
import com.example.today_i_dressedup.data.Post
import com.example.today_i_dressedup.data.User
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import java.io.File

class FirebaseSource {

    companion object {
        @Volatile
        private var instance: FirebaseSource? = null

        @JvmStatic
        fun getInstance(): FirebaseSource =
            instance ?: synchronized(this) {
                instance ?: FirebaseSource().also {
                    instance = it
                }
            }
    }

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val firebaseStorage: FirebaseStorage by lazy{
        FirebaseStorage.getInstance()
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
                if (it.isSuccessful) {
                    insertUserToDB(it.result!!.user!!.uid)
                    emitter.onComplete()
                } else {
                    emitter.onError(it.exception!!)
                }
            }
        }
    }

    fun insertUserToDB(userId: String) {
        val user = User(userId)
        firebaseFirestore
            .collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener { Log.d("FirebaseSource", "insertUserToDB Success") }
            .addOnFailureListener { Log.d("FirebaseSource", "insertUserToDB Fail") }
    }

    fun uploadPost(filePath: String){
        var file = Uri.fromFile(File(filePath))
        val riversRef = firebaseStorage.reference.child("images/${file.lastPathSegment}")
        val uploadTask = riversRef.putFile(file)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            riversRef.downloadUrl.addOnSuccessListener {
                Log.d("FirebaseSource", it.toString())
                val post = Post(currentUser()!!.uid, it.toString())
                firebaseFirestore
                    .collection("posts")
                    .add(post)
                    .addOnSuccessListener {   } }
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

}