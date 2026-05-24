package com.example.eshorts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.eshorts.ui.theme.EshortsTheme
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.eshorts.data.repository.ShortsRepository
import com.example.eshorts.data.navigation.AppNavGraph
import com.example.eshorts.viewmodel.ShopViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = ShortsRepository(this)
        val viewModel = ShopViewModel(repository)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}