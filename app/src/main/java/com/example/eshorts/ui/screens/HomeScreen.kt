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
import com.example.eshorts.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ShopViewModel,
    onOpenDetail: (Int) -> Unit,
    onOpenCart: () -> Unit
) {
    val state = viewModel.productsState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ешорты") },
                actions = { TextButton(onClick = onOpenCart) { Text("Корзина") } }
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
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(product.name)
                            Text("${product.price} €")
                            Row {
                                Button(onClick = { viewModel.addToCart(product) }) {
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