package com.shimsoon.today_i_dressedup.ui.postDetail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.data.Post
import com.shimsoon.today_i_dressedup.databinding.ActivityPostDetailBinding
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : AppCompatActivity() {

    private lateinit var post: Post
    private lateinit var tvNumOfLike: TextView
    private lateinit var tvNumOfDislike: TextView
    private lateinit var ivImg: ImageView

    private lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_detail)
        getPostFromIntent()
        initView()
        showPost()
    }

    private fun getPostFromIntent() {
        post = intent.getSerializableExtra("post") as Post
    }

    private fun showPost() {
        Glide.with(this)
            .load(post.imgUrl)
            .into(ivImg)
        tvNumOfLike.text = post.numOfLike.toString()
        tvNumOfDislike.text = post.numOfDislike.toString()
        showGenderRatio()
    }

    private fun showGenderRatio(){
        with(post){
            val likeMaleRatio = if(numOfLike > 0) numOfLikeMale / numOfLike.toFloat() else 0.toFloat()
            val likeFemaleRatio = if(numOfLike > 0) numOfLikeFemale / numOfLike.toFloat() else 0.toFloat()
            val dislikeMaleRatio = if(numOfDislike > 0) numOfDislikeMale / numOfDislike.toFloat() else 0.toFloat()
            val dislikeFemaleRatio = if(numOfDislike > 0) numOfDislikeFemale / numOfDislike.toFloat() else 0.toFloat()

            binding.textViewLikeRatio.text = getString(R.string.gender_ratio_format, likeMaleRatio * 100, likeFemaleRatio * 100)
            binding.textViewDislikeRatio.text = getString(R.string.gender_ratio_format, dislikeMaleRatio * 100, dislikeFemaleRatio * 100)
        }
    }

    private fun initView() {
        tvNumOfLike = postDetailActivity_tv_numOfLike
        tvNumOfDislike = postDetailActivity_tv_numOfDislike
        ivImg = postDetailActivity_iv_img
    }
}
