package com.example.stockia.view.salesOrder

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.R
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.salesOrder.SalesOrdersViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.stockia.common.SalesOrderCard



@Composable
fun SalesOrdersView(
    navController: NavController,
    viewModel: SalesOrdersViewModel = viewModel()


) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedOrderId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(viewModel.resultMessage) {
        viewModel.resultMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearResultMessage()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSalesOrders()
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderWithBackArrow(
                text = "Órdenes de venta",
                onClick = {
                    navController.navigate(Routes.HomeView) {
                        popUpTo(Routes.SalesOrdersView) { inclusive = true }
                    }
                }

            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = viewModel.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    label = "Buscar orden",
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = "Nuevo",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigate(Routes.NewSalesOrderView)
                        }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (viewModel.isLoading) {
                Text(text = "Cargando órdenes...", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (viewModel.filteredOrders.isEmpty()) {
                Text(text = "No se encontraron órdenes.", modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(viewModel.filteredOrders) { order ->
                        SalesOrderCard(
                            order = order,
                            onConfirmClick = {
                                if (order.status_name == "pending") {
                                    selectedOrderId = order.id
                                    showDialog = true
                                }
                            }
                        )
                    }
                }
            }

        }
        if (showDialog && selectedOrderId != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.confirmOrder(selectedOrderId!!)
                        showDialog = false
                    }) {
                        Text("Confirmar", color = Color(0xFF4CAF50))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                title = {
                    Text(
                        text = "Confirmar orden",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                text = {
                    Text(
                        text = "¿Deseas confirmar la orden de venta No. ${selectedOrderId}?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )

        }

    }
}



