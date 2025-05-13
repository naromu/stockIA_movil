package com.example.stockia.view.salesReturn

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.*
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.viewmodel.salesReturn.FinalizeSalesReturnViewModel

@Composable
fun FinalizeSalesReturnView(
    navController: NavController,
    selectedProducts: String,
    orderId: Int,
    viewModel: FinalizeSalesReturnViewModel = viewModel()
) {
    val context = LocalContext.current
    val productIds = remember(selectedProducts) {
        selectedProducts.split(",").mapNotNull { it.toIntOrNull() }
    }

    // Cargar productos seleccionados de la orden
    LaunchedEffect(orderId, selectedProducts) {
        viewModel.loadProductsFromOrder(orderId, productIds)
    }

    // Mostrar Toast y redirigir si es exitoso
    LaunchedEffect(viewModel.isSuccess) {
        if (viewModel.isSuccess) {
            Toast.makeText(context, "Devolución creada exitosamente", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.SalesReturnsView) {
                popUpTo(Routes.SalesReturnsView) { inclusive = true }
            }
        }
    }

    // Popup de error
    if (viewModel.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(viewModel.errorMessage ?: "") },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("Cerrar")
                }
            }
        )
    }

    // Vista principal
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoBase)
            .systemBarsPadding()
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Título con flecha
            HeaderWithBackArrow(
                text = "Nueva devolución de venta",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Por favor especifique la cantidad y el estado del producto a devolver",
                style = AppTypography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo descripción
            Text(
                text = "Descripción",
                style = AppTypography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            CustomTextField(
                value = viewModel.notes,
                onValueChange = { viewModel.updateNotes(it) },
                label = "Descripción",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de productos seleccionados
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(viewModel.items) { item ->
                    ProductReturnCard(
                        productName = item.productName,
                        productDescription = item.productDescription,
                        quantity = item.quantity,
                        maxQuantity = item.maxQuantity,
                        statusId = item.statusId,
                        onQuantityChange = { viewModel.updateQuantity(item.productId, it) },
                        onStatusChange = { viewModel.updateStatus(item.productId, it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de finalizar
            CustomButtonBlue(
                text = "Finalizar devolución",
                onClick = { viewModel.createSalesReturn(orderId) }
            )
        }
    }
}
