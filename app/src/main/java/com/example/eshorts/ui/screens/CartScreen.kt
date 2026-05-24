package com.example.eshorts.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eshorts.ui.formatRubles
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
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                }
            )
        }
    ) { padding ->
        when {
            state.isLoading -> CircularProgressIndicator(modifier = Modifier.padding(padding))
            state.error != null -> Text(
                "Ошибка: ${state.error}",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )

            else -> {
                val products = state.data ?: emptyList()
                // общая стоимость: сумма price * quantity
                val total = products.sumOf { it.price * it.quantity }

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(products) { product ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(product.name)

                                        // цена за штуку
                                        Text("Цена: ${formatRubles(product.price)}")

                                        // количество
                                        Text("Кол-во: ${product.quantity}")

                                        // сумма по позиции
                                        val lineTotal = product.price * product.quantity
                                        Text("Итого: ${formatRubles(lineTotal)}")
                                    }

                                    Column {
                                        Button(onClick = { viewModel.addToCart(product) }) {
                                            Text("+")
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Button(onClick = { viewModel.removeFromCart(product) }) {
                                            Text("-")
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Общая сумма: ${formatRubles(total)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}