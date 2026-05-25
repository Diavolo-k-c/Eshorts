package com.example.eshorts.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.eshorts.ui.screens.CartScreen
import com.example.eshorts.ui.screens.FavoritesScreen
import com.example.eshorts.ui.screens.HomeScreen
import com.example.eshorts.ui.screens.DetailScreen
import com.example.eshorts.viewmodel.ShopViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart

enum class BottomDestination {
    Favorites, Home, Cart
}

@Composable
fun EshortsApp(viewModel: ShopViewModel) {
    var currentDestination by remember { mutableStateOf(BottomDestination.Home) }
    var selectedProductId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination == BottomDestination.Favorites,
                    onClick = {
                        currentDestination = BottomDestination.Favorites
                        selectedProductId = null
                    },
                    icon = { Icon(Icons.Filled.FavoriteBorder, contentDescription = "Избранное") },
                    label = { Text("Избранное") }
                )
                NavigationBarItem(
                    selected = currentDestination == BottomDestination.Home,
                    onClick = {
                        currentDestination = BottomDestination.Home
                        selectedProductId = null
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Товары") },
                    label = { Text("Товары") }
                )
                NavigationBarItem(
                    selected = currentDestination == BottomDestination.Cart,
                    onClick = {
                        currentDestination = BottomDestination.Cart
                        selectedProductId = null
                    },
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Корзина") },
                    label = { Text("Корзина") }
                )
            }
        }
    ) { innerPadding ->
        when (currentDestination) {
            BottomDestination.Home -> {
                if (selectedProductId == null) {
                    HomeScreen(
                        viewModel = viewModel,
                        onOpenDetail = { id -> selectedProductId = id },
                        onOpenCart = { currentDestination = BottomDestination.Cart }
                    )
                } else {
                    DetailScreen(
                        productId = selectedProductId!!,
                        viewModel = viewModel,
                        onBack = { selectedProductId = null }
                    )
                }
            }

            BottomDestination.Cart -> {
                CartScreen(
                    viewModel = viewModel,
                    onBack = { currentDestination = BottomDestination.Home }
                )
            }

            BottomDestination.Favorites -> {
                FavoritesScreen(
                    viewModel = viewModel,
                    onBack = { currentDestination = BottomDestination.Home },
                    onOpenDetail = { id ->
                        selectedProductId = id
                        currentDestination = BottomDestination.Home
                    }
                )
            }
        }
    }
}