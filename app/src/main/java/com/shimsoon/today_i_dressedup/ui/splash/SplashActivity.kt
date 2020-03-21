package com.shimsoon.today_i_dressedup.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.ui.auth.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this, LoginActivity::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            delay(1300)
            startActivity(intent)
            finish()
        }
    }
}
