package com.shimsoon.today_i_dressedup.ui.myPage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.data.Status
import com.shimsoon.today_i_dressedup.ui.postList.PostActivity
import com.squareup.okhttp.Dispatcher
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

class MyPageActivity : AppCompatActivity() {

    private lateinit var btn_myUpload: MaterialButton
    private lateinit var btn_likeFashion: Button
    private lateinit var btn_dislikeFashion: Button
    private lateinit var fab_add: FloatingActionButton
    private lateinit var progressBar: ProgressBar

    private lateinit var myPageViewModel: MyPageViewModel

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()
    companion object {
        const val PUT_EXTRA_KEY = "request_type"
        const val PUT_EXTRA_FOR_MY_FASHION = 100
        const val PUT_EXTRA_FOR_LIKE_FASHION = 101
        const val PUT_EXTRA_FOR_DISLIKE_FASHION = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        initView()
        observe()
    }

    private fun observe() {
        myPageViewModel.getUploadLiveStatus().observe(this, Observer {
            when (it) {
                Status.SUCCESS -> {
                    hideProgressBar()
                    Toast.makeText(applicationContext, "업로드 성공", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    showProgressBar()
                }
                Status.FAILURE -> {
                    hideProgressBar()
                    Toast.makeText(applicationContext, "업로드 실패", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun initView() {
        myPageViewModel = ViewModelProviders.of(this).get(MyPageViewModel::class.java)

        btn_myUpload = myPageActivity_btn_myUpload
        btn_myUpload.setOnClickListener {
            myPageViewModel.loadMyPosts()
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra(PUT_EXTRA_KEY, PUT_EXTRA_FOR_MY_FASHION)
            startActivity(intent)
        }

        btn_likeFashion = myPageActivity_btn_likeFashion
        btn_likeFashion.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra(PUT_EXTRA_KEY, PUT_EXTRA_FOR_LIKE_FASHION)
            startActivity(intent)
        }

        btn_dislikeFashion = myPageActivity_btn_dislikeFashion
        btn_dislikeFashion.setOnClickListener {
            val intent = Intent(this, PostActivity::class.java)
            intent.putExtra(PUT_EXTRA_KEY, PUT_EXTRA_FOR_DISLIKE_FASHION)
            startActivity(intent)
        }

        fab_add = myPageActivity_fab_add
        fab_add.setOnClickListener { pickPhoto() }

        progressBar = myPageActivity_progressBar
    }

    private fun pickPhoto() {
        ImagePicker.create(this)
            .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
            .folderMode(true) // folder mode (false by default)
            .toolbarFolderTitle("Folder") // folder selection title
            .toolbarImageTitle("Tap to select") // image selection title
            .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
            .includeVideo(true) // Show video on image picker
            .multi()
            .limit(1) // max images can be selected (99 by default)
            .showCamera(true) // show camera or not (true by default)
            .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
            .enableLog(false) // disabling log
            .start() // start image picker activity with request code
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            val images = ImagePicker.getImages(data)
            for (image in images) {
                Log.d("picked", image.path)
                //memoId는 MemoListActivity에서 memo를 db에 저장한 후 리턴되는 id값을 이용해서 다시 세팅함.
                val disposable = myPageViewModel
                    .compressFile(File(image.path))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe ({
                        myPageViewModel.uploadPostToServer(it)
                    }){
                        Log.e("MyPageActivity", "Image Compression error")
                    }
                compositeDisposable.add(disposable)
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
