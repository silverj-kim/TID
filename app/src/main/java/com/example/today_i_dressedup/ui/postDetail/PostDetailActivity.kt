package com.example.today_i_dressedup.ui.postDetail

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.today_i_dressedup.R
import com.example.today_i_dressedup.data.Post
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : AppCompatActivity() {

    private lateinit var post: Post
    private lateinit var tv_numOfLike: TextView
    private lateinit var tv_numOfDislike: TextView
    private lateinit var iv_img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        getPostFromIntent()
        initView()
        showPost()
    }

    fun getPostFromIntent() {
        post = intent.getSerializableExtra("post") as Post
    }

    fun showPost() {
        Glide.with(this)
            .load(post.imgUrl)
            .into(iv_img)
        tv_numOfLike.text = post.numOfLike.toString()
        tv_numOfDislike.text = post.numOfDislike.toString()
    }

    fun initView() {
        tv_numOfLike = postDetailActivity_tv_numOfLike
        tv_numOfDislike = postDetailActivity_tv_numOfDislike
        iv_img = postDetailActivity_iv_img
    }
}
