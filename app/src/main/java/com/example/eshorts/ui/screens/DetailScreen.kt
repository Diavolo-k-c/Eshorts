package com.example.eshorts.ui.screens
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eshorts.viewmodel.ShopViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    productId: Int,
    viewModel: ShopViewModel,
    onBack: () -> Unit
) {
    val product = viewModel.productsState.collectAsStateWithLifecycle().value.data?.find { it.id == productId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали товара") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Назад") } }
            )
        }
    ) { padding ->
        if (product != null) {
            Text(
                text = "${product.name}\n\n${product.description}\n\nЦена: ${product.price} €",
                modifier = Modifier.padding(padding).padding(16.dp)
            )
        } else {
            Text("Товар не найден", modifier = Modifier.padding(16.dp))
        }
    }
}