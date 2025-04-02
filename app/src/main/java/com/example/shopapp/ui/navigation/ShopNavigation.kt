package com.example.shopapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopapp.ui.screens.cart.CartScreen
import com.example.shopapp.ui.screens.checkout.CheckoutScreen
import com.example.shopapp.ui.screens.home.HomeScreen
import com.example.shopapp.ui.screens.initial.InitialChoiceScreen
import com.example.shopapp.ui.screens.login.LoginScreen
import com.example.shopapp.ui.screens.product.ProductScreen
import com.example.shopapp.ui.screens.security.SecurityCodeScreen
import com.example.shopapp.ui.screens.settings.SettingsScreen

sealed class Screen(val route: String) {
    object InitialChoice : Screen("initial_choice")
    object Login : Screen("login")
    object SecurityCode : Screen("security_code")
    object Home : Screen("home")
    object Product : Screen("product/{productId}") {
        fun createRoute(productId: String) = "product/$productId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object Settings : Screen("settings")
}

@Composable
fun ShopNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.InitialChoice.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.InitialChoice.route) {
            InitialChoiceScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.SecurityCode.route) {
            SecurityCodeScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Product.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            requireNotNull(productId) { "productId parameter wasn't found. Please make sure it's set!" }
            ProductScreen(navController, productId)
        }
        composable(Screen.Cart.route) {
            CartScreen(navController)
        }
        composable(Screen.Checkout.route) {
            CheckoutScreen(navController)
        }
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
} 