package com.shimsoon.today_i_dressedup.data.firebase

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.shimsoon.today_i_dressedup.data.Post
import com.shimsoon.today_i_dressedup.data.Status
import com.shimsoon.today_i_dressedup.data.User
import com.shimsoon.today_i_dressedup.util.Secret
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.File
import java.io.IOException


class FirebaseSource {

    val liveUploadState: MutableLiveData<Status> = MutableLiveData()

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()

    }

    private val firebaseStorage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val firebaseInstanceId: FirebaseInstanceId by lazy {
        FirebaseInstanceId.getInstance()
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

    fun updateUserToken() {
        firebaseInstanceId.instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FirebaeSource", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                firebaseFirestore
                    .collection("users")
                    .document(currentUser()!!.uid)
                    .update("token", token)
                    .addOnSuccessListener { Log.d("FirebaseSource", "updateUserToken Success") }
                    .addOnFailureListener { Log.d("FirebaseSource", "updateUserToken Fail") }
            })
    }

    fun uploadPost(file: File) {
        liveUploadState.value = Status.LOADING
        var file = Uri.fromFile(file)
        val riversRef = firebaseStorage.reference.child("images/${file.lastPathSegment}")
        val uploadTask = riversRef.putFile(file)

        uploadTask.addOnFailureListener {
            liveUploadState.value = Status.FAILURE
        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            riversRef.downloadUrl.addOnSuccessListener {
                Log.d("FirebaseSource", it.toString())
                val post = Post(userId = currentUser()!!.uid, imgUrl = it.toString())
                firebaseFirestore
                    .collection("posts")
                    .add(post)
                    .addOnSuccessListener {
                        liveUploadState.value = Status.SUCCESS
                        it.update("timeStamp", FieldValue.serverTimestamp())

                        firebaseFirestore
                            .collection("users")
                            .document(currentUser()!!.uid)
                            .update("post_ids", FieldValue.arrayUnion(it.id))
                    }
            }
        }
    }

//    fun uploadPost(filePath: String) {
//        liveUploadState.value = Status.LOADING
//        var file = Uri.fromFile(File(filePath))
//        val riversRef = firebaseStorage.reference.child("images/${file.lastPathSegment}")
//        val uploadTask = riversRef.putFile(file)
//        uploadTask.addOnFailureListener {
//            liveUploadState.value = Status.FAILURE
//        }.addOnSuccessListener {
//            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//            // ...
//            riversRef.downloadUrl.addOnSuccessListener {
//                Log.d("FirebaseSource", it.toString())
//                val post = Post(userId = currentUser()!!.uid, imgUrl = it.toString())
//                firebaseFirestore
//                    .collection("posts")
//                    .add(post)
//                    .addOnSuccessListener {
//                        liveUploadState.value = Status.SUCCESS
//                        it.update("timeStamp", FieldValue.serverTimestamp())
//
//                        firebaseFirestore
//                            .collection("users")
//                            .document(currentUser()!!.uid)
//                            .update("post_ids", FieldValue.arrayUnion(it.id))
//                    }
//            }
//        }
//    }

    fun loadAllPosts() = Observable.create<List<Post>> { emitter ->
        var query = firebaseFirestore
            .collection("posts")
            .orderBy("timeStamp", Query.Direction.ASCENDING)
            .limit(50)

        query.get()
            .addOnSuccessListener { documnets ->
                val list: ArrayList<Post> = ArrayList()
                for (documnet in documnets) {
                    val post = documnet.toObject(Post::class.java)
                    Log.d("FirebaseSource", post.userId + ", " + currentUser()!!.uid)
                    if (!post.voters.contains(currentUser()!!.uid)) { //내가 이미 투표한 포스트면 제외
                        if (post.userId != currentUser()!!.uid) { //내가 올린 post는 평가화면에서 제외.
                            post.id = documnet.id
                            list.add(post)
                        }
                    }
                }
                emitter.onNext(list)

//                //5개씩 페이징.
//                val lastVisible = documnets.documents[documnets.size() - 1]
//                query = firebaseFirestore
//                    .collection("posts")
//                    .orderBy("timeStamp", Query.Direction.ASCENDING)
//                    .startAfter(lastVisible)
//                    .limit(5)
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
            .addOnSuccessListener { documents ->
                emitter.onNext(documents.toObjects(Post::class.java))
            }
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(it.exception!!)
                }
            }
    }

    fun loadPostILiked() = Observable.create<List<Post>> { emitter ->
        var likeIdList: List<String>?
        firebaseFirestore
            .collection("users")
            .document(currentUser()!!.uid)
            .get()
            .addOnSuccessListener {
                val currentUser = it.toObject(User::class.java)
                likeIdList = currentUser!!.like_post_ids

                firebaseFirestore
                    .collection("posts")
                    .whereArrayContains("voters", currentUser()!!.uid)
                    .get()
                    .addOnSuccessListener { documents ->
                        val listILiked: MutableList<Post> = ArrayList()
                        for (document in documents) {
                            if (likeIdList!!.contains(document.id)) {
                                listILiked.add(document.toObject(Post::class.java))
                            }
                        }
                        emitter.onNext(listILiked)
                    }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }

            }
    }

    fun loadPostIDisliked() = Observable.create<List<Post>> { emitter ->
        var dislikeIdList: List<String>
        firebaseFirestore
            .collection("users")
            .document(currentUser()!!.uid)
            .get()
            .addOnSuccessListener {
                val currentUser = it.toObject(User::class.java)
                dislikeIdList = currentUser!!.dislike_post_ids

                firebaseFirestore
                    .collection("posts")
                    .whereArrayContains("voters", currentUser()!!.uid)
                    .get()
                    .addOnSuccessListener { documents ->
                        val listIDisliked: MutableList<Post> = ArrayList()
                        for (document in documents) {
                            if (dislikeIdList.contains(document.id)) {
                                listIDisliked.add(document.toObject(Post::class.java))
                            }
                        }
                        emitter.onNext(listIDisliked)
                    }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(it.exception!!)
                        }
                    }
            }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

    fun likePost(postId: String) {
        //postId의 numOfLike를 +1 해주고 완료시 voters에 현재유저의 id를 추가
        firebaseFirestore
            .collection("posts")
            .document(postId)
            .get()
            .addOnSuccessListener { document ->
                val updates = hashMapOf<String, Any>(
                    "numOfLike" to document.get("numOfLike") as Long + 1,
                    "timeStamp" to FieldValue.serverTimestamp()
                )
                firebaseFirestore.collection("posts")
                    .document(postId)
                    .update(updates)
                    .addOnSuccessListener {
                        firebaseFirestore
                            .collection("posts")
                            .document(postId)
                            .update("voters", FieldValue.arrayUnion(currentUser()!!.uid))
                    }
            }

        firebaseFirestore
            .collection("users")
            .document(currentUser()!!.uid)
            .update("like_post_ids", FieldValue.arrayUnion(postId)) //like_post_ids배열에 좋아요한 post의 id를 추가함
    }

    fun dislikePost(postId: String) {
        //postId의 numOfDislike를 +1 해주고 완료시 voters에 현재유저의 id를 추가
        firebaseFirestore
            .collection("posts")
            .document(postId)
            .get()
            .addOnSuccessListener { document ->
                val updates = hashMapOf<String, Any>(
                    "numOfDislike" to document.get("numOfDislike") as Long + 1,
                    "timeStamp" to FieldValue.serverTimestamp()
                )
                firebaseFirestore.collection("posts")
                    .document(postId)
                    .update(updates)
                    .addOnSuccessListener {
                        firebaseFirestore
                            .collection("posts")
                            .document(postId)
                            .update("voters", FieldValue.arrayUnion(currentUser()!!.uid))
                    }
            }

        firebaseFirestore
            .collection("users")
            .document(currentUser()!!.uid)
            .update("dislike_post_ids", FieldValue.arrayUnion(postId)) //dislike_post_ids배열에 싫어요한 post의 id를 추가함
    }

    fun sendNotification(userId: String, postId: String) {
        val apiKey = Secret.google_fcm_key
        firebaseFirestore
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { it ->
                val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                val token = it.get("token").toString()
                Log.d("FirebaseSource token=", token)

                val json = JSONObject()
                val notiJson = JSONObject()
                val dataJson = JSONObject()
                notiJson.put("body", "포스트가 평가되었습니다.")
                notiJson.put("title", "today_i_dressedup")
                dataJson.put("postId", postId)
                json.put("notification", notiJson)
                json.put("to", token)
                json.put("data", dataJson)

                val body = RequestBody?.create(JSON, json.toString())

                val okHttpClient = OkHttpClient.Builder().build()

                val request = Request.Builder()
                    .url("https://fcm.googleapis.com/fcm/send")
                    .header("Content-Type", "application/json")
                    .addHeader("Authorization", "key=" + apiKey)
                    .post(body)
                    .build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("FirebaseSource", "sendNotification Fail")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            Log.d("FirebaseSource", "sendNotification Success")
                        } else {
                            Log.d("FirebaseSource", "sendNoti Suc,Fail")
                            Log.d("FirebaseSource", response.code.toString() + ", " + response.message)
                        }
                    }
                })
            }
    }
}