package com.example.trabalhopraticocm.api

data class Report(
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val titulo: String,
    val descr: String,
    val img: String,
    val user_id: Int
)
