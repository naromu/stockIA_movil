package com.example.stockia.routes

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stockia.routes.Routes.CreateProductView
import com.example.stockia.routes.Routes.NewSalesOrderView
import com.example.stockia.routes.Routes.ProductsView
import com.example.stockia.utils.LogoutEventBus
import com.example.stockia.utils.SharedPreferencesHelper
import com.example.stockia.utils.isJwtExpired
import com.example.stockia.view.categories.CategoriesView
import com.example.stockia.view.categories.EditCategoryView
import com.example.stockia.view.categories.createCategoryView
import com.example.stockia.view.login.ConfirmEmailView
import com.example.stockia.view.login.HomeView
import com.example.stockia.view.login.LoginView
import com.example.stockia.view.login.RegisterView
import com.example.stockia.view.login.ResetPasswordOneView
import com.example.stockia.view.login.ResetPasswordThreeView
import com.example.stockia.view.login.ResetPasswordTwoView
import com.example.stockia.view.clients.ClientsView
import com.example.stockia.view.clients.EditClientView
import com.example.stockia.view.clients.NewClientView
import com.example.stockia.view.providers.ProvidersView
import com.example.stockia.view.providers.NewProviderView
import com.example.stockia.view.providers.EditProviderView
import com.example.stockia.view.salesOrder.SalesOrdersView
import com.example.stockia.view.salesOrder.NewSalesOrderView


import com.example.stockia.view.products.CreateProductView
import com.example.stockia.view.products.CreateProductViewPreview
import com.example.stockia.view.products.EditProductView
import com.example.stockia.view.products.ProductsView
import com.example.stockia.view.purchasesOrder.CompletePurchaseOrderView
import com.example.stockia.view.purchasesOrder.NewPurchasesOrderView
import com.example.stockia.view.purchasesOrder.PredictionView
import com.example.stockia.view.purchasesOrder.PurchasesOrdersView
import com.example.stockia.view.salesOrder.CompleteSalesOrderView
import com.example.stockia.view.stock.StockListView
import com.example.stockia.view.stock.EditStockView


@Composable
fun AppNavHost(context: Context) {
    val navController = rememberNavController()

    val prefs = remember { SharedPreferencesHelper(context) }
    val token: String? = remember { prefs.getSessionToken() }

    val isSessionInvalid = token.isNullOrBlank() || token.isJwtExpired()

    LaunchedEffect(isSessionInvalid) {
        if (isSessionInvalid) {
            prefs.clearSession()
        }
    }

    val startDestination = if (isSessionInvalid) {
        Routes.LoginView
    } else {
        Routes.HomeView
    }

    LaunchedEffect(Unit) {
        LogoutEventBus.events.collect {
            Toast.makeText(context, "Sesión expirada", Toast.LENGTH_SHORT).show()

            navController.navigate(Routes.LoginView) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.LoginView) {
            LoginView(navController)
            BackHandler {  }
        }

        composable(Routes.RegisterView){
            RegisterView(navController)
            BackHandler { }
        }

        composable(Routes.ConfirmEmailView){
            ConfirmEmailView(navController)
            BackHandler { }
        }

        composable(Routes.HomeView){
            HomeView(navController)
            BackHandler { }
        }

        composable(Routes.ResetPasswordOneView){
            ResetPasswordOneView(navController)
            BackHandler { }
        }

        composable(Routes.ResetPasswordTwoView){
            ResetPasswordTwoView(navController)
            BackHandler { }
        }

        composable(
            route = "${Routes.ResetPasswordThreeView}?code={code}",
            arguments = listOf(
                    navArgument("code") { type = NavType.StringType; defaultValue = "" })
            ){
            backStackEntry ->
            val code = backStackEntry.arguments?.getString("code") ?: ""

            ResetPasswordThreeView(navController, code = code)
            BackHandler { }
        }

        composable(Routes.CategoriesView){
            CategoriesView(navController)
            BackHandler { }
        }

        composable(Routes.createCategoryView){
            createCategoryView(navController)
            BackHandler { }
        }

        composable(
            route = "${Routes.EditCategoryView}/{categoryId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("categoryId")
            EditCategoryView(navController = navController, categoryId = id)
            BackHandler { }

        }

        composable(Routes.ClientsView) {
            ClientsView(navController)
            BackHandler { }
        }

        composable(Routes.NewClientView) {
            NewClientView(navController)
            BackHandler { }
        }

        composable(
            route = "${Routes.EditClientView}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            EditClientView(navController = navController, clientId = id)
            BackHandler { }
        }

        // PROVEEDORES
        composable(Routes.ProvidersView) {
            ProvidersView(navController)
            BackHandler { }
        }

        composable(Routes.NewProviderView) {
            NewProviderView(navController)
            BackHandler { }
        }

        composable(
            route = "${Routes.EditProviderView}/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            EditProviderView(navController = navController, providerId = id)
            BackHandler { }
        }

        //Stock
        composable("StockListView") {
            StockListView(navController)
        }

        composable(
            route = "EditStockView/{productId}/{currentStock}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType },
                navArgument("currentStock") { type = NavType.IntType }
            )
        ) {
            val productId = it.arguments?.getInt("productId") ?: 0
            val currentStock = it.arguments?.getInt("currentStock") ?: 0
            EditStockView(navController, productId, currentStock)
        }




        //SALES ORDERS
//        composable(Routes.SalesOrdersView) {
//            SalesOrdersView(navController)
//            BackHandler { }
//        }

        composable(
            route = "${Routes.SalesOrdersView}?message={message}",
            arguments = listOf(
                navArgument("message") { nullable = true }
            )
        ) { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message")
            SalesOrdersView(navController = navController, initialMessage = message)
        }


        composable(Routes.NewSalesOrderView){
            NewSalesOrderView(navController)
            BackHandler { }
        }

        composable(
            route = Routes.CompleteSalesOrderViewWithArgs,
            arguments = listOf(
                navArgument("selectedProducts") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val selectedProductIds = backStackEntry.arguments
                ?.getString("selectedProducts")
                ?.takeIf { it.isNotEmpty() }
                ?.split(",")
                ?.mapNotNull { it.toIntOrNull() }
                ?: emptyList()

            CompleteSalesOrderView(navController = navController, selectedProductIds = selectedProductIds)
            BackHandler { }
        }

        // Purchase Orders
        composable(Routes.PurchasesOrdersView) {
            PurchasesOrdersView(navController)
            BackHandler { }
        }

        composable(Routes.NewPurchaseOrderView){
            NewPurchasesOrderView(navController)
            BackHandler { }
        }

        composable(Routes.CompletePurchasesOrderViewWithArgs) {
            CompletePurchaseOrderView(navController = navController)
        }






        //Products
        composable(Routes.ProductsView){
            ProductsView(navController)
            BackHandler { }
        }
        composable(Routes.CreateProductView){
            CreateProductView(navController)
            BackHandler { }
        }

        composable(
            route = "${Routes.EditProductView}/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("productId")
            EditProductView(navController = navController, productId = id)
            BackHandler { }

        }

        //IA

        composable(Routes.PredictionView){
            PredictionView(navController)
            BackHandler { }
        }

    }
}
