package com.shimsoon.today_i_dressedup.util

import android.content.Context
import android.content.Intent
import com.shimsoon.today_i_dressedup.ui.auth.LoginActivity
import com.shimsoon.today_i_dressedup.ui.evaluate.EvaluateActivity

fun Context.startHomeActivity() =
    Intent(this, EvaluateActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }