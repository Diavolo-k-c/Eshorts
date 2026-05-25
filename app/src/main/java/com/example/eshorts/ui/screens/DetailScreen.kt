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
    onBack: () -> Unit
) {
    val state = viewModel.productsState.collectAsStateWithLifecycle().value
    val product = state.data?.find { it.id == productId }

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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

    // список картинок из модели
    val images = product.imageUrls.ifEmpty { listOf("") }

    // индекс текущей картинки
    var currentIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Блок с картинкой и стрелками
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                AsyncImage(
                    model = images[currentIndex],
                    contentDescription = "${product.name} фото ${currentIndex + 1}",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Левая стрелка
                if (images.size > 1) {
                    IconButton(
                        onClick = {
                            currentIndex =
                                if (currentIndex == 0) images.size - 1 else currentIndex - 1
                        },
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Предыдущее фото"
                        )
                    }

                    // Правая стрелка
                    IconButton(
                        onClick = {
                            currentIndex =
                                if (currentIndex == images.size - 1) 0 else currentIndex + 1
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Следующее фото"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Индикатор (номер фото)
            Text(
                text = "${currentIndex + 1} / ${images.size}",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = formatRubles(product.price),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = product.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewModel.addToCart(product)
                        scope.launch {
                            snackbarHostState.showSnackbar("Товар добавлен в корзину")
                        }
                    }
                ) {
                    Text("В корзину")
                }

                OutlinedButton(
                    onClick = {
                        viewModel.toggleFavorite(product)
                    }
                ) {
                    Text(if (product.isFavorite) "Убрать из избранного" else "В избранное")
                }
            }
        }
    }
}