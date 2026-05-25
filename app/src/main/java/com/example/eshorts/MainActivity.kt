package com.example.eshorts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eshorts.data.navigation.AppNavGraph
import com.example.eshorts.data.navigation.Routes
import com.example.eshorts.data.repository.ShortsRepository
import com.example.eshorts.viewmodel.ShopViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = ShortsRepository(this)
        val viewModel = ShopViewModel(repository)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentRoute == Routes.FAVORITES,
                                onClick = {
                                    navController.navigate(Routes.FAVORITES) {
                                        popUpTo(Routes.HOME) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.FavoriteBorder,
                                        contentDescription = "Избранное"
                                    )
                                },
                                label = { Text("Избранное") }
                            )
                            NavigationBarItem(
                                selected = currentRoute == Routes.HOME ||
                                        currentRoute?.startsWith(Routes.DETAIL) == true,
                                onClick = {
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(Routes.HOME) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.Home,
                                        contentDescription = "Товары"
                                    )
                                },
                                label = { Text("Товары") }
                            )
                            NavigationBarItem(
                                selected = currentRoute == Routes.CART,
                                onClick = {
                                    navController.navigate(Routes.CART) {
                                        popUpTo(Routes.HOME) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Filled.ShoppingCart,
                                        contentDescription = "Корзина"
                                    )
                                },
                                label = { Text("Корзина") }
                            )
                        }
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        viewModel = viewModel,
                        innerPadding = innerPadding,
                        onShowAddedToCart = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Товар добавлен в корзину")
                            }
                        }
                    )
                }
            }
        }
    }
}