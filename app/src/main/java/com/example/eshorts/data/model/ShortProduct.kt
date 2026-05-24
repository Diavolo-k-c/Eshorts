package com.example.eshorts.data.model

data class ShortProduct(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrl: String = "",
    val inCart: Boolean = false,
    val isFavorite: Boolean = false,
    val quantity: Int = 0
)