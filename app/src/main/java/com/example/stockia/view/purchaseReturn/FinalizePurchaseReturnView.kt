package com.example.stockia.view.purchaseReturn

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.QuantitySelectorCard
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.purchaseReturn.FinalizePurchaseReturnViewModel

@Composable
fun FinalizePurchaseReturnView(
    navController: NavController,
    selectedProducts: String,
    orderId: Int,
    viewModel: FinalizePurchaseReturnViewModel = viewModel()
) {
    val context = LocalContext.current
    val selectedIds = remember(selectedProducts) {
        selectedProducts.split(",").mapNotNull { it.toIntOrNull() }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSelectedProducts(orderId, selectedIds)
    }

    // Éxito: mostrar Toast y volver
    LaunchedEffect(viewModel.isSuccess) {
        if (viewModel.isSuccess) {
            Toast.makeText(context, "Devolución creada", Toast.LENGTH_SHORT).show()
            navController.navigate(Routes.PurchaseReturnsView) {
                popUpTo(Routes.PurchaseReturnsView) { inclusive = true }
            }
        }
    }

    // Error: mostrar popup
    if (viewModel.errorMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("Aceptar")
                }
            },
            title = { Text("Error") },
            text = { Text(viewModel.errorMessage ?: "Error desconocido") }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoBase)
            .systemBarsPadding()
            .padding(24.dp)
    ) {
        Column {
            HeaderWithBackArrow(
                text = "Finalizar devolución de compra",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Por favor especifique la cantidad y la nota de la devolución",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Campo descripción
            Text(text = "Descripción", style = MaterialTheme.typography.bodyMedium)
            CustomTextField(
                value = viewModel.notes,
                onValueChange = viewModel::onNotesChange,
                label = "Notas",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Productos con cantidad editable
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                items(viewModel.products) { product ->
                    QuantitySelectorCard(
                        productName = product.product_name ?: "Producto",
                        productDescription = product.product_description ?: "",
                        quantity = viewModel.quantities[product.product_id] ?: 1,
                        onIncrease = { viewModel.changeQuantity(product.product_id, +1) },
                        onDecrease = { viewModel.changeQuantity(product.product_id, -1) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomButtonBlue(
                text = "Finalizar devolución",
                onClick = { viewModel.submitReturn(orderId) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
