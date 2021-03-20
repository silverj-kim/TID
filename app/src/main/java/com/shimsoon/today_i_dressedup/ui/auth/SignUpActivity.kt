package com.shimsoon.today_i_dressedup.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.databinding.ActivitySignupBinding
import com.shimsoon.today_i_dressedup.util.startHomeActivity
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity(), AuthListener {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        init()
    }

    fun init() {
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.viewmodel = authViewModel
        authViewModel.authListener = this
    }

    override fun onStarted() {
        displayProgressBar(true)
    }

    override fun onSuccess() {
        displayProgressBar(false)
        startHomeActivity()
    }

    override fun onFailure(message: String) {
        displayProgressBar(false)
        Log.d("SignupActivity", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayProgressBar(visible: Boolean) {
        binding.progressBar.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}
