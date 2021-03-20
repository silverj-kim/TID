package com.shimsoon.today_i_dressedup.network

sealed class NetworkState {
    class Init : NetworkState()
    class Loading : NetworkState()
    class Success() : NetworkState()
    class Error(val throwable: Throwable?) : NetworkState()
}