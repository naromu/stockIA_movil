package com.example.stockia.view.login

import HomeViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.R
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.StockIATheme


@Composable
fun HomeView(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()


) {
    val context = LocalContext.current

    LaunchedEffect(homeViewModel.resultMessage) {
        when (homeViewModel.resultMessage) {
            "success" -> {
                Toast.makeText(context, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show()
                homeViewModel.clearMessage()
                // Navega al login
                navController?.navigate(Routes.LoginView) {
                    popUpTo(Routes.HomeView) { inclusive = true }
                }
            }

            null -> Unit

            else -> {
                Toast.makeText(context, homeViewModel.resultMessage!!, Toast.LENGTH_LONG).show()
                homeViewModel.clearMessage()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Asegúrate de tener `logo.png` en res/drawable
                contentDescription = "Logo",
                modifier = Modifier
                    .size(150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¡Bienvenido a StockIA!",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))


            CustomButtonBlue(
                text = if (homeViewModel.isLoading) "Cerrando Sesion..." else "Cerrar sesión",
                onClick = { homeViewModel.onLogoutClick() },
                enabled = !homeViewModel.isLoading
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    StockIATheme {
        HomeView(navController= rememberNavController())
    }
}