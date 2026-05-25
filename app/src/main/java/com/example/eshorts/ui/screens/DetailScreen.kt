package com.example.eshorts.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.eshorts.ui.formatRubles
import com.example.eshorts.viewmodel.ShopViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productId: Int,
    viewModel: ShopViewModel,
    onBack: () -> Unit,
    onShowAddedToCart: () -> Unit
) {
    val state = viewModel.productsState.collectAsStateWithLifecycle().value
    val product = state.data?.find { it.id == productId }

    if (product == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Детали товара") },
                    navigationIcon = {
                        TextButton(onClick = onBack) { Text("Назад") }
                    }
                )
            }
        ) { padding ->
            Text(
                "Товар не найден",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewModel.addToCart(product)
                        onShowAddedToCart()
                    }
                ) {
                    Text("В корзину")
                }

                OutlinedButton(
                    onClick = { viewModel.toggleFavorite(product) }
                ) {
                    Text(if (product.isFavorite) "Убрать из избранного" else "В избранное")
                }
            }
        }
    }}