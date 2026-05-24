package com.example.eshorts.ui.screens

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
fun CartScreen(
    viewModel: ShopViewModel,
    onBack: () -> Unit
) {
    val state = viewModel.cartState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Корзина") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Назад") } }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.padding(padding))
            state.error != null -> Text("Ошибка: ${state.error}", modifier = Modifier.padding(16.dp))
            else -> LazyColumn(modifier = Modifier.padding(padding)) {
                items(state.data ?: emptyList()) { product ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name)
                                Text("${product.price} €")
                            }
                            Button(onClick = { viewModel.removeFromCart(product) }) {
                                Text("Убрать")
                            }
                        }
                    }
                }
            }
        }
    }
}