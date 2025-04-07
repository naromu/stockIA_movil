package com.example.stockia.routes

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stockia.routes.Routes.LoginView
import com.example.stockia.routes.Routes.RegisterView
import com.example.stockia.routes.Routes.ResetPasswordOneView
import com.example.stockia.view.login.ConfirmEmailView
import com.example.stockia.view.login.HomePreview
import com.example.stockia.view.login.HomeView
import com.example.stockia.view.login.LoginView
import com.example.stockia.view.login.RegisterView
import com.example.stockia.view.login.ResetPasswordOnePreview
import com.example.stockia.view.login.ResetPasswordOneView
import com.example.stockia.view.login.ResetPasswordThreeView
import com.example.stockia.view.login.ResetPasswordTwoView
import okhttp3.Route


@Composable
fun AppNavHost(context: Context) {
    val navController = rememberNavController()

    // Ejemplo de efecto global como cierre de sesión
    LaunchedEffect(Unit) {
        // Simulación de evento global (puedes usar StateFlow o EventBus)
        // launch { yourEventBus.collect { navController.navigate("login") } }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LoginView
    ) {
        composable(Routes.LoginView) {
            LoginView(navController)
            BackHandler { /* bloquear retroceso si es necesario */ }
        }

        composable(Routes.RegisterView){
            RegisterView(navController)
            BackHandler { /* bloquear retroceso si es necesario */ }
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


    }
}
