package com.shimsoon.today_i_dressedup.ui.auth

interface AuthListener {
    fun onStarted()

    fun onSuccess()

    fun onFailure(message: String)
}