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
import com.example.stockia.utils.LogoutEventBus
import com.example.stockia.utils.SharedPreferencesHelper
import com.example.stockia.utils.isJwtExpired
import com.example.stockia.view.categories.CategoriesView
import com.example.stockia.view.categories.EditCategoryView
import com.example.stockia.view.categories.newCategoryView
import com.example.stockia.view.login.ConfirmEmailView
import com.example.stockia.view.login.HomeView
import com.example.stockia.view.login.LoginView
import com.example.stockia.view.login.RegisterView
import com.example.stockia.view.login.ResetPasswordOneView
import com.example.stockia.view.login.ResetPasswordThreeView
import com.example.stockia.view.login.ResetPasswordTwoView


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

        composable(Routes.newCategoryView){
            newCategoryView(navController)
            BackHandler { }
        }

        composable(
            route = "${Routes.EditCategoryView}/{categoryId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // extraes el ID y lo pasas al Composable
            val id = backStackEntry.arguments!!.getInt("categoryId")
            EditCategoryView(navController = navController, categoryId = id)
            BackHandler { }

        }


    }
}
