package com.example.today_i_dressedup.ui.postList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.today_i_dressedup.R
import com.example.today_i_dressedup.data.Post
import com.example.today_i_dressedup.data.repository.PostRepository
import com.example.today_i_dressedup.ui.myPage.MyPageActivity
import com.example.today_i_dressedup.ui.postDetail.PostDetailActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_post.*

class PostActivity : AppCompatActivity(), PostClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var postViewModel: PostViewModel

    private lateinit var postList: List<Post>
    private var requestType = 0

    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        getRequestTypeFromIntent()
        initViewModel()
        when (requestType) {
            MyPageActivity.PUT_EXTRA_FOR_MY_FASHION -> {
                loadMyPost()
            }
            MyPageActivity.PUT_EXTRA_FOR_LIKE_FASHION -> {
                loadPostILiked()
            }
            MyPageActivity.PUT_EXTRA_FOR_DISLIKE_FASHION -> {
                loadPostIDisliked()
            }
        }
    }

    fun getRequestTypeFromIntent() {
        requestType = intent.getIntExtra(MyPageActivity.PUT_EXTRA_KEY, -1)
    }

    fun loadMyPost() {
        val disposable = postViewModel
            .loadMyPost()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                for (post in it) {
                    Log.d("PostActivity", post.toString())
                }
                postList = it
                initView()
            })

        disposables.add(disposable)
    }

    fun loadPostILiked() {
        val disposable = postViewModel
            .loadPostILiked()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                for (post in it) {
                    Log.d("PostActivity", post.toString())
                }
                postList = it
                initView()
            })

        disposables.add(disposable)
    }

    fun loadPostIDisliked() {
        val disposable = postViewModel
            .loadPostIDisliked()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                for (post in it) {
                    Log.d("PostActivity", post.toString())
                }
                postList = it
                initView()
            })

        disposables.add(disposable)
    }

    fun initViewModel() {
        val factory = PostViewModelFactory(PostRepository.getInstance())
        postViewModel = ViewModelProviders.of(this, factory).get(PostViewModel::class.java)
    }

    fun initView() {
        recyclerView = postActivity_recyclerView
        adapter = PostAdapter(this, postList, this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
    }

    override fun onPostClicked(post: Post) {
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra("post", post)
        startActivity(intent)
    }
}
