package com.shimsoon.today_i_dressedup.ui.postList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.data.Post
import kotlinx.android.synthetic.main.post_list_item.view.*

class PostAdapter(val postClickListener: PostClickListener, var postList: List<Post>, var context: Context) :
    RecyclerView.Adapter<PostAdapter.PostHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PostHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_list_item, p0, false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(p0: PostHolder, p1: Int) {
        val cosmetics = postList[p1]
        cosmetics?.let {
            p0.bind(it, context)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class PostHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivThumbnail = view.postList_iv_thumbnail
        val tvNumOfDislike = view.postList_tv_numOfDisLike
        val tvNumOfLike = view.postList_tv_numOfLike

        fun bind(post: Post, context: Context) {
            tvNumOfDislike.text = post.numOfDislike.toString()
            tvNumOfLike.text = post.numOfLike.toString()
            val option = RequestOptions().centerCrop().override(520).transform(RoundedCorners(36))
            Glide
                .with(context)
                .load(post.imgUrl)
                .apply(option)
                .into(ivThumbnail)
            itemView.setOnClickListener {
                postClickListener.onPostClicked(post)
            }
        }
    }
}