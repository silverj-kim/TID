package com.shimsoon.today_i_dressedup.ui.postList

import com.shimsoon.today_i_dressedup.data.Post

interface PostClickListener {
    fun onPostClicked(post: Post)
}