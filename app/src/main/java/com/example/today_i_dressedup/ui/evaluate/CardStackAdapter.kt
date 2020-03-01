package com.example.today_i_dressedup.ui.evaluate

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.today_i_dressedup.R
import com.example.today_i_dressedup.data.Post
import com.example.today_i_dressedup.ui.postDetail.PostDetailActivity
import com.example.today_i_dressedup.ui.postList.PostActivity

class CardStackAdapter(private var posts: List<Post> = emptyList()) :
    RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.item_spot,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun setPosts(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    fun getPosts(): List<Post> {
        return posts
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.item_image)
        fun bind(post: Post) {
            Glide.with(image)
                .load(post.imgUrl)
                .into(image)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, PostDetailActivity::class.java)
                intent.putExtra("post", post)
                itemView.context.startActivity(intent)
            }
        }
    }

}