package com.example.eshorts.ui.state

data class UiState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String? = null
)