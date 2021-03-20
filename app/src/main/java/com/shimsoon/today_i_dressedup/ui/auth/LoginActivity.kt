package com.shimsoon.today_i_dressedup.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.databinding.ActivityLoginBinding
import com.shimsoon.today_i_dressedup.util.startHomeActivity

class LoginActivity : AppCompatActivity(), AuthListener {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
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

    private fun displayProgressBar(visible: Boolean) {
        binding.progressBar.visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onFailure(message: String) {
        displayProgressBar(false)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        authViewModel.user?.let {
            startHomeActivity()
        }
    }
}
