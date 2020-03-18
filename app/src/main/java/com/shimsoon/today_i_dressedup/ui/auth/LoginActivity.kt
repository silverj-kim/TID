package com.shimsoon.today_i_dressedup.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.shimsoon.today_i_dressedup.R
import com.shimsoon.today_i_dressedup.databinding.ActivityLoginBinding
import com.shimsoon.today_i_dressedup.util.startHomeActivity
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), AuthListener {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
//        factory = AuthViewModelFactory(UserRepository.getInstance())
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel::class.java)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar() {
        progressbar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressbar.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        authViewModel.user?.let {
            startHomeActivity()
        }
    }
}
