package com.example.stockia.view.clients

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.R
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.viewmodel.clients.ClientViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.example.stockia.common.CategoryTextButton

@Composable
fun ClientsView(
    navController: NavController,
    clientViewModel: ClientViewModel = viewModel()
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    // Mostrar mensajes del ViewModel
    LaunchedEffect(clientViewModel.resultMessage) {
        clientViewModel.resultMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            clientViewModel.clearResultMessage()
        }
    }

    // Recargar clientes cada vez que regresas a esta pantalla
    LaunchedEffect(navController.currentBackStackEntry) {
        clientViewModel.loadClients()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(BlancoBase)
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            HeaderWithBackArrow(
                text = "Clientes",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = clientViewModel.searchClient,
                    onValueChange = clientViewModel::onSearchClientChange,
                    label = "Buscar cliente",
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = "Nuevo",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigate(Routes.NewClientView)
                        }
                )
            }

            Spacer(Modifier.height(16.dp))

            val isRefreshing = clientViewModel.isLoading

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { clientViewModel.loadClients() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clientViewModel.filteredClients) { client ->
                        CategoryTextButton(
                            text = client.name,
                            onClick = {
                                navController.navigate("${Routes.EditClientView}/${client.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientsViewPreview() {
    StockIATheme {
        ClientsView(navController = rememberNavController())
    }
}
