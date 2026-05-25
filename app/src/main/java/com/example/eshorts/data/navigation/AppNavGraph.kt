package com.example.eshorts.data.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eshorts.ui.screens.CartScreen
import com.example.eshorts.ui.screens.DetailScreen
import com.example.eshorts.ui.screens.HomeScreen
import com.example.eshorts.ui.screens.FavoritesScreen
import com.example.eshorts.viewmodel.ShopViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: ShopViewModel,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onOpenDetail = { id ->
                    navController.navigate("${Routes.DETAIL}/$id")
                },
                onOpenCart = {
                    navController.navigate(Routes.CART)
                }
            )
        }
        composable("${Routes.DETAIL}/{productId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("productId")?.toIntOrNull() ?: 0
            DetailScreen(
                productId = id,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.CART) {
            CartScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.FAVORITES) {
            FavoritesScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onOpenDetail = { id ->
                    navController.navigate("${Routes.DETAIL}/$id")
                }
            )
        }
    }
}