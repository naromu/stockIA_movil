package com.example.stockia.view.providers

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.R
import com.example.stockia.common.CategoryTextButton
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.providers.ProviderViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun ProvidersView(
    navController: NavController,
    providerViewModel: ProviderViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(providerViewModel.resultMessage) {
        providerViewModel.resultMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            providerViewModel.clearResultMessage()
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        providerViewModel.loadProviders()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(BlancoBase)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderWithBackArrow(text = "Proveedores") {
                navController.popBackStack()
            }

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = providerViewModel.searchProvider,
                    onValueChange = providerViewModel::onSearchProviderChange,
                    label = "Buscar proveedor",
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Nuevo proveedor",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable { navController.navigate(Routes.NewProviderView) }
                )
            }

            Spacer(Modifier.height(16.dp))

            val isRefreshing = providerViewModel.isLoading
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { providerViewModel.loadProviders() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(providerViewModel.filteredProviders) { provider ->
                        CategoryTextButton(
                            text = provider.name,
                            onClick = {
                                navController.navigate("${Routes.EditProviderView}/${provider.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
