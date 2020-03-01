package com.example.today_i_dressedup.ui.postDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.today_i_dressedup.R
import com.example.today_i_dressedup.data.Post

class PostDetailActivity : AppCompatActivity() {

    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        getPostFromIntent()
        Log.d("PostDetailActivity", post.toString())
    }

    fun getPostFromIntent(){
        post = intent.getSerializableExtra("post") as Post
    }
}
