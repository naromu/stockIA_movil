package com.example.stockia.view.purchasesOrder

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.stockia.common.PurchaseOrderCard
import com.example.stockia.common.SalesOrderCard
import com.example.stockia.viewmodel.purchaseOrder.PurchaseOrdersViewModel

@Composable
fun PurchasesOrdersView(
    navController: NavController,
    viewModel: PurchaseOrdersViewModel = viewModel()
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedOrderId by remember { mutableStateOf<Int?>(null) }

    // Mostrar toasts de resultado
    LaunchedEffect(viewModel.resultMessage) {
        viewModel.resultMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearResultMessage()
        }
    }
    // Cargar al iniciar
    LaunchedEffect(Unit) { viewModel.loadPurchaseOrders() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(BlancoBase)
            .padding(24.dp)
    ) {
        Column {
            HeaderWithBackArrow(
                text = "Órdenes de compra",
                onClick = {
                    navController.navigate(Routes.HomeView) {
                        popUpTo(Routes.PurchasesOrdersView) { inclusive = true }
                    }
                }
            )
            Spacer(Modifier.height(16.dp))
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
                            navController.navigate(Routes.NewPurchaseOrderView)
                        }
                )
            }
            Spacer(Modifier.height(24.dp))

            // ─── Leyenda de estados ───────────────────────────────────────────────────
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pendiente (amarillo)
                Box(
                    Modifier
                        .size(12.dp)
                        .background(color = Color(0xFFFFA000), shape = CircleShape)
                )
                Spacer(Modifier.width(4.dp))
                Text("Pendiente", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.width(16.dp))

                // Confirmada (verde)
                Box(
                    Modifier
                        .size(12.dp)
                        .background(color = Color(0xFF4CAF50), shape = CircleShape)
                )
                Spacer(Modifier.width(4.dp))
                Text("Confirmada", style = MaterialTheme.typography.bodyMedium)

            }
            // ────────────────────────────────────────────────────────────────────────────
            Spacer(Modifier.height(16.dp))


            when {
                viewModel.isLoading -> Text("Cargando órdenes...", modifier = Modifier.align(Alignment.CenterHorizontally))
                viewModel.filteredOrders.isEmpty() -> Text("No hay órdenes.", modifier = Modifier.align(Alignment.CenterHorizontally))
                else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.filteredOrders) { order ->
                        PurchaseOrderCard(
                            order = order,
                            onConfirmClick = { clickedOrder ->
                                selectedOrderId = clickedOrder.id
                                showDialog = true
                            }
                        )
                        Spacer(Modifier.height(8.dp))

                    }
                }
            }

        }
        Spacer(Modifier.height(24.dp))


        if (showDialog && selectedOrderId != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirmar orden") },
                text  = { Text("¿Confirmar la orden No. $selectedOrderId?") },
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
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}
