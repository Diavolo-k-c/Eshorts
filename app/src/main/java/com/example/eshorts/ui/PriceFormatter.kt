package com.example.eshorts.ui

fun formatRubles(price: Double): String {
    // Округление до двух знаков
    val rounded = String.format("%.2f", price)
    return "$rounded ₽"
}