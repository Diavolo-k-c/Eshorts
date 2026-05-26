package com.example.eshorts.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.eshorts.ui.formatRubles
import com.example.eshorts.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ShopViewModel,
    onOpenDetail: (Int) -> Unit,
    onOpenCart: () -> Unit,
    onOpenAccount: () -> Unit,
    onShowAddedToCart: () -> Unit
) {
    val state = viewModel.productsState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ешорты") },
                actions = {
                    TextButton(onClick = onOpenAccount) {
                        Text("Аккаунт")
                    }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.padding(padding))
            state.error != null -> Text("Ошибка: ${state.error}", modifier = Modifier.padding(16.dp))
            else -> LazyColumn(modifier = Modifier.padding(padding)) {
                items(state.data ?: emptyList()) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { onOpenDetail(product.id) }
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            AsyncImage(
                                model = product.imageUrls.firstOrNull().orEmpty(),
                                contentDescription = product.name,
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name)
                                Text(formatRubles(product.price))
                                Row {
                                    Button(
                                        onClick = {
                                            viewModel.addToCart(product)
                                            onShowAddedToCart()   // ← сказать наверх показать снекбар
                                        }
                                    ) {
                                        Text("В корзину")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OutlinedButton(onClick = { viewModel.toggleFavorite(product) }) {
                                        Text("Избранное")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}