package com.example.stockia.view.purchasesOrder

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.*
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.purchaseOrder.CompletePurchaseOrderViewModel

@Composable
fun CompletePurchaseOrderView(
    navController: NavController,
    viewModel: CompletePurchaseOrderViewModel = viewModel()
) {


    val ids: List<Int> = navController
        .previousBackStackEntry
        ?.savedStateHandle
        ?.get<List<Int>>("selectedProducts")
        ?: emptyList()

    LaunchedEffect(ids) {
        viewModel.loadSelectedProducts(ids)
    }

    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .systemBarsPadding()
            .background(BlancoBase)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            HeaderWithBackArrow(
                text = "Nueva Orden de Compra",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown: Proveedor
            FilterableDropdown(
                label = "Proveedor",
                options = uiState.suppliers.map { it.name },
                selectedOption = uiState.selectedSupplier?.name,
                onOptionSelected = { name -> viewModel.onSupplierSelected(name) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown: Estado de la compra
            FilterableDropdown(
                label = "Estado de la compra",
                options = uiState.statusOptions.map { it.description },
                selectedOption = uiState.selectedStatus?.description,
                onOptionSelected = { desc -> viewModel.onStatusSelected(desc) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo descripción
            Text(
                text = "Descripción",
                style = AppTypography.bodyLarge,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            CustomTextField(
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = "Descripción",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen
            Text("Resumen", style = AppTypography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.selectedProducts) { product ->
                    ProductCardWithQuantity(
                        name = product.name,
                        description = product.description,
                        imageUrl = product.imageUrl,
                        unitPrice = product.unitPrice.toDoubleOrNull() ?: 0.0,
                        quantity = product.quantity,
                        onIncrease = { viewModel.increaseQuantity(product.id) },
                        onDecrease = { viewModel.decreaseQuantity(product.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Total y botón
            Text(
                text = "Total: $${"%.2f".format(uiState.totalAmount)}",
                style = AppTypography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            val isFormValid = uiState.selectedSupplier != null && uiState.selectedStatus != null
            CustomButtonBlue(
                text = "Generar compra",
                onClick = { viewModel.submitPurchaseOrder(navController) },
                enabled = isFormValid
            )
        }
    }
}
