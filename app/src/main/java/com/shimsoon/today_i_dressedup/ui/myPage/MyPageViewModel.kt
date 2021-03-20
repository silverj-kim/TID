package com.shimsoon.today_i_dressedup.ui.myPage

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import com.shimsoon.today_i_dressedup.data.repository.PostRepository
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import io.reactivex.Observable
import kotlinx.coroutines.*
import java.io.File

class MyPageViewModel(application: Application) : AndroidViewModel(application) {

    private val postRepository: PostRepository = PostRepository.getInstance()

    fun uploadPostToServer(file: File) {
        postRepository.uploadPost(file)
    }

    fun compressFile(file: File) = Observable.create<File> { emitter ->
        GlobalScope.launch {
            val compressedImageFile = Compressor.compress(getApplication<Application>().applicationContext, file) {
                default(width = 480, height = 480, format = Bitmap.CompressFormat.JPEG)
            }
            emitter.onNext(compressedImageFile)
        }
    }


    fun loadMyPosts() {
        postRepository.loadMyPost()
    }

    fun getUploadLiveStatus() = postRepository.getLiveUploadStatus()
}