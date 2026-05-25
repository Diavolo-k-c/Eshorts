package com.example.eshorts.data.model

data class ShortProduct(
    val id: Int = 0,
    val name: String,
    val price: Double,
    val description: String,
    val imageUrlsRaw: String = "",
    val inCart: Boolean = false,
    val isFavorite: Boolean = false,
    val quantity: Int = 0
) {
    val imageUrls: List<String>
        get() = imageUrlsRaw
            .split(";")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
}