package com.shimsoon.today_i_dressedup.ui.postList

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.data.Post
import com.shimsoon.today_i_dressedup.data.repository.PostRepository
import com.shimsoon.today_i_dressedup.ui.myPage.MyPageActivity
import com.shimsoon.today_i_dressedup.ui.postDetail.PostDetailActivity
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

    private fun getRequestTypeFromIntent() {
        requestType = intent.getIntExtra(MyPageActivity.PUT_EXTRA_KEY, -1)
    }

    private fun loadMyPost() {
        val disposable = postViewModel
            .loadMyPost()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postList = it
                initView()
            }, {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            })

        disposables.add(disposable)
    }

    private fun loadPostILiked() {
        val disposable = postViewModel
            .loadPostILiked()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postList = it
                initView()
            }, {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            })

        disposables.add(disposable)
    }

    private fun loadPostIDisliked() {
        val disposable = postViewModel
            .loadPostIDisliked()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                postList = it
                initView()
            }, {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            })

        disposables.add(disposable)
    }

    private fun initViewModel() {
        postViewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
    }

    private fun initView() {
        if (postList.size > 0) {
            postActivity_tv_noItem.visibility = View.GONE
        } else {
            postActivity_tv_noItem.visibility = View.VISIBLE
        }
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

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}
