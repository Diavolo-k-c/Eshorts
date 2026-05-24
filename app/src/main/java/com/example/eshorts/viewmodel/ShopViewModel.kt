package com.example.eshorts.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eshorts.data.model.ShortProduct
import com.example.eshorts.data.repository.ShortsRepository
import com.example.eshorts.ui.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShopViewModel(
    private val repository: ShortsRepository
) : ViewModel() {

    private val _productsState = MutableStateFlow(UiState<List<ShortProduct>>(isLoading = true))
    val productsState: StateFlow<UiState<List<ShortProduct>>> = _productsState

    private val _cartState = MutableStateFlow(UiState<List<ShortProduct>>(isLoading = true))
    val cartState: StateFlow<UiState<List<ShortProduct>>> = _cartState

    init {
        loadProducts()
        loadCart()
    }

    fun loadProducts() {
        viewModelScope.launch {
            try {
                _productsState.value = UiState(isLoading = true)
                val products = repository.getAllProducts()
                _productsState.value = UiState(data = products)
            } catch (e: Exception) {
                _productsState.value = UiState(error = "Ошибка загрузки товаров")
            }
        }
    }

    fun loadCart() {
        viewModelScope.launch {
            try {
                _cartState.value = UiState(isLoading = true)
                val cart = repository.getCartProducts()
                _cartState.value = UiState(data = cart)
            } catch (e: Exception) {
                _cartState.value = UiState(error = "Ошибка загрузки корзины")
            }
        }
    }

    fun addToCart(product: ShortProduct) {
        viewModelScope.launch {
            try {
                repository.updateProduct(product.copy(inCart = true))
                loadProducts()
                loadCart()
            } catch (e: Exception) {
                _productsState.value = _productsState.value.copy(error = "Не удалось добавить в корзину")
            }
        }
    }

    fun toggleFavorite(product: ShortProduct) {
        viewModelScope.launch {
            try {
                repository.updateProduct(product.copy(isFavorite = !product.isFavorite))
                loadProducts()
            } catch (e: Exception) {
                _productsState.value = _productsState.value.copy(error = "Не удалось изменить избранное")
            }
        }
    }

    fun removeFromCart(product: ShortProduct) {
        viewModelScope.launch {
            try {
                repository.updateProduct(product.copy(inCart = false))
                loadProducts()
                loadCart()
            } catch (e: Exception) {
                _cartState.value = _cartState.value.copy(error = "Не удалось удалить из корзины")
            }
        }
    }
}