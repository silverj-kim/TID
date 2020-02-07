package com.example.today_i_dressedup

data class Spot(
        val id: Long = counter++,
        val name: String,
        val city: String,
        val url: String
) {
    companion object {
        private var counter = 0L
    }
}