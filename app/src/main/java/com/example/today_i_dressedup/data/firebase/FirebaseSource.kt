package com.example.today_i_dressedup.data.firebase

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.today_i_dressedup.data.Post
import com.example.today_i_dressedup.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.File

class FirebaseSource {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val firebaseStorage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

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

    fun uploadPost(filePath: String) {
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
                val post = Post(userId = currentUser()!!.uid, imgUrl = it.toString())
                firebaseFirestore
                    .collection("posts")
                    .add(post)
                    .addOnSuccessListener { }
            }
        }
    }

    fun loadAllPosts() = Observable.create<List<Post>> { emitter ->
        firebaseFirestore
            .collection("posts")
            .get()
            .addOnSuccessListener { documnets ->
                val list: ArrayList<Post> = ArrayList()
                for(documnet in documnets){
                    val post = documnet.toObject(Post::class.java)
                    post.id = documnet.id
                    list.add(post)
                }
                emitter.onNext(list)
            }
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(it.exception!!)
                }
            }
    }

    fun loadMyPost() = Observable.create<List<Post>> { emitter ->
        firebaseFirestore
            .collection("posts")
            .whereEqualTo("userId", currentUser()!!.uid)
            .get()
            .addOnSuccessListener {documents ->
                emitter.onNext(documents.toObjects(Post::class.java))
            }
            .addOnCompleteListener {
                if(it.isSuccessful){
                    emitter.onComplete()
                }else{
                    emitter.onError(it.exception!!)
                }
            }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun likePost(postId: String) {
        firebaseFirestore
            .collection("posts")
            .document(postId)
            .get()
            .addOnSuccessListener { document ->
                firebaseFirestore.collection("posts")
                    .document(postId)
                    .update("numOfLike", document.get("numOfLike") as Long + 1)
            }

        firebaseFirestore
            .collection("users")
            .document(currentUser()!!.uid)
            .update("like_post_ids", FieldValue.arrayUnion(postId)) //like_post_ids배열에 좋아요한 post의 id를 추가함
    }

    fun dislikePost(postId: String) {
        firebaseFirestore
            .collection("posts")
            .document(postId)
            .get()
            .addOnSuccessListener { document ->
                firebaseFirestore.collection("posts")
                    .document(postId)
                    .update("numOfDislike", document.get("numOfDislike") as Long + 1)
            }

        firebaseFirestore
            .collection("users")
            .document(currentUser()!!.uid)
            .update("dislike_post_ids", FieldValue.arrayUnion(postId)) //dislike_post_ids배열에 싫어요한 post의 id를 추가함
    }

}