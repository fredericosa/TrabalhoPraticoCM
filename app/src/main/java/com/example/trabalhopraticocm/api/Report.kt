package com.example.trabalhopraticocm.api

data class Report(
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val title: String,
    val descricao: String,
    val imagem: String,
    val user_id: Int
)
