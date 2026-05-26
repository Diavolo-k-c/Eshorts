package com.example.eshorts.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.eshorts.ui.formatRubles
import com.example.eshorts.viewmodel.ShopViewModel

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

            if (product.imageUrls.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    product.imageUrls.forEach { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = product.name,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(260.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Цена
            Text(
                text = formatRubles(product.price),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Описание товара
            if (!product.description.isNullOrBlank()) {
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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
                    Text(
                        if (product.isFavorite)
                            "Убрать из избранного"
                        else
                            "В избранное"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}