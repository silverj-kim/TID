package com.shimsoon.today_i_dressedup.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.data.repository.UserRepository
import com.shimsoon.today_i_dressedup.databinding.ActivitySignupBinding
import com.shimsoon.today_i_dressedup.util.startHomeActivity
import kotlinx.android.synthetic.main.activity_signup.*


class SignUpActivity : AppCompatActivity(), AuthListener {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var factory: AuthViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        init()
    }

    fun init(){
        factory = AuthViewModelFactory(UserRepository.getInstance())
        authViewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        val binding: ActivitySignupBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.viewmodel = authViewModel
        authViewModel.authListener = this
    }

    override fun onStarted() {
        showProgressBar()
    }

    override fun onSuccess() {
        hideProgressBar()
        startHomeActivity()
    }

    override fun onFailure(message: String) {
        hideProgressBar()
        Log.d("SignupActivity", message)
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressBar(){
        progressbar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        progressbar.visibility = View.GONE
    }
}
