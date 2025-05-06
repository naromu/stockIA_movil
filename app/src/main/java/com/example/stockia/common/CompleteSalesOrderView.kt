package com.example.stockia.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.FilterableDropdown
import com.example.stockia.common.ProductCardWithQuantity
import com.example.stockia.common.CustomTextField
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.viewmodel.salesOrder.CompleteSalesOrderViewModel

@Composable
fun CompleteSalesOrderView(
    navController: NavController,
    viewModel: CompleteSalesOrderViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Dropdown: Cliente
        FilterableDropdown(
            label = "Cliente",
            options = uiState.clients.map { it.name },
            selectedOption = uiState.selectedClient?.name,
            onOptionSelected = { name -> viewModel.onClientSelected(name) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown: Estado de venta
        FilterableDropdown(
            label = "Estado de la venta",
            options = uiState.statusOptions.map { it.description },
            selectedOption = uiState.selectedStatus?.description,
            onOptionSelected = { desc ->
                viewModel.onStatusSelected(desc)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo descripción
        CustomTextField(
            value = uiState.description,
            onValueChange = { viewModel.onDescriptionChange(it) },
            label = "Descripción",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

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
                    unitPrice = product.unitPrice.toDoubleOrNull() ?: 0.0,
                    quantity = product.quantity,
                    onIncrease = { viewModel.increaseQuantity(product.id) },
                    onDecrease = { viewModel.decreaseQuantity(product.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Total y botón
        Text(
            text = "Total: $${"%.2f".format(uiState.totalAmount)}",
            style = AppTypography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        CustomButtonBlue(
            text = "Generar compra",
            onClick = { viewModel.submitSalesOrder(navController) }
        )
    }
}
