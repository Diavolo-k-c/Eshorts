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
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    viewModel: ShopViewModel,
    onBack: () -> Unit,
    onOpenDetail: (Int) -> Unit
) {
    val state = viewModel.productsState.collectAsStateWithLifecycle().value
    val favorites = state.data?.filter { it.isFavorite } ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Избранное") },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Назад") }
                }
            )
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Text(
                "Избранных товаров пока нет",
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(8.dp)
            ) {
                items(favorites) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onOpenDetail(product.id) }
                    ) {
                        Row(modifier = Modifier.padding(16.dp)) {
                            AsyncImage(
                                model = product.imageUrls.firstOrNull().orEmpty(),
                                contentDescription = product.name,
                                modifier = Modifier.size(80.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(product.name)
                                Text(formatRubles(product.price))
                            }
                        }
                    }
                }
            }
        }
    }
}