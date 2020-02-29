package com.example.today_i_dressedup.ui.evaluate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.today_i_dressedup.R
import com.example.today_i_dressedup.data.Post

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

    fun setSpots(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    fun getSpots(): List<Post> {
        return posts
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.item_image)

        fun bind(post: Post) {
            Glide.with(image)
                .load(post.imgUrl)
                .into(image)
            itemView.setOnClickListener { Toast.makeText(itemView.context, post.userId, Toast.LENGTH_SHORT).show() }
        }
    }

}