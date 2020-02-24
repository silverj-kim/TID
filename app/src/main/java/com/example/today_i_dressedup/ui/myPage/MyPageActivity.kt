package com.example.today_i_dressedup.ui.myPage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.example.today_i_dressedup.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_my_page.*

class MyPageActivity : AppCompatActivity() {

    private lateinit var btn_myUpload: Button
    private lateinit var btn_likeFashion: Button
    private lateinit var btn_dislikeFashion: Button
    private lateinit var fab_add: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        initView()
    }

    fun initView() {
        btn_myUpload = myPageActivity_btn_myUpload
        btn_likeFashion = myPageActivity_btn_likeFashion
        btn_dislikeFashion = myPageActivity_btn_dislikeFashion
        fab_add = myPageActivity_fab_add
        fab_add.setOnClickListener {pickPhoto()}
    }

    fun pickPhoto(){
        ImagePicker.create(this)
            .returnMode(ReturnMode.NONE) // set whether pick and / or camera action should return immediate result or not.
            .folderMode(true) // folder mode (false by default)
            .toolbarFolderTitle("Folder") // folder selection title
            .toolbarImageTitle("Tap to select") // image selection title
            .toolbarArrowColor(Color.BLACK) // Toolbar 'up' arrow color
            .includeVideo(true) // Show video on image picker
            .multi()
            .limit(10) // max images can be selected (99 by default)
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
                //memoId는 MemoListActivity에서 memo를 db에 저장한 후 리턴되는 id값을 이용해서 다시 세팅함.
            }
            Log.d("picked", images.toString())
        }
    }
}
