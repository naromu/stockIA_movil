package com.example.stockia.view.login

import HomeViewModel
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.R
import com.example.stockia.common.HeaderWithHamburger
import com.example.stockia.common.IconTextButton
import com.example.stockia.common.SideMenu
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.utils.SharedPreferencesHelper

@Composable
fun HomeView(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current

    var menuOpen by remember { mutableStateOf(false) }

    var headerHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    val headerHeightDp = with(density) { headerHeightPx.toDp() }

    val company = remember { SharedPreferencesHelper(context).getCompanyName() }

    LaunchedEffect(homeViewModel.resultMessage) {
        when (homeViewModel.resultMessage) {
            "success" -> {
                Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                navController.navigate(Routes.LoginView) {
                    popUpTo(Routes.HomeView) { inclusive = true }
                }
                homeViewModel.clearMessage()
            }
            null -> Unit
            else -> {
                Toast.makeText(context, homeViewModel.resultMessage, Toast.LENGTH_SHORT).show()
                homeViewModel.clearMessage()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(BlancoBase)
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        if (!menuOpen) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = headerHeightDp + 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconTextButton(
                    text = "Categorías",
                    iconRes = R.drawable.categorias,
                    onClick = {
                        navController.navigate(Routes.CategoriesView)
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                IconTextButton(
                    text = "Productos",
                    iconRes = R.drawable.productos,
                    onClick = {
                        navController.navigate(Routes.ProductsView)

                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
                IconTextButton(
                    text = "Stock",
                    iconRes = R.drawable.stock,
                    onClick = {
                        Toast.makeText(context, "En desarrollo 🚧", Toast.LENGTH_SHORT).show()

                    }
                )
            }
        }

        if (menuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = headerHeightDp)
                    .clickable { menuOpen = false }
            )
        }

        AnimatedVisibility(
            visible = menuOpen,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            ),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = headerHeightDp)
        ) {
            SideMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                currentRoute = navController.currentBackStackEntry?.destination?.route ?: "",
                nameUser = company,
                onRouteClick = { route ->
                    menuOpen = false
                    navController.navigate(route)
                },
                onLogoutClick = {
                    menuOpen = false
                    homeViewModel.onLogoutClick()
                }
            )
        }

        HeaderWithHamburger(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .onGloballyPositioned { coords ->
                    headerHeightPx = coords.size.height
                },
            text = "Inventario",
            isMenuOpen = menuOpen,
            onClick = { menuOpen = !menuOpen }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    StockIATheme {
        HomeView(navController = rememberNavController())
    }
}
