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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.ReturnedProductCard
import com.example.stockia.common.UpdateSalesReturnButton
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.salesReturn.SalesReturnDetailViewModel

@Composable
fun SalesReturnDetailView(
    navController: NavController,
    returnId: Int,
    viewModel: SalesReturnDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showFirstDialog by remember { mutableStateOf(false) }

    // Mostrar el popup cuando haya un producto seleccionado
    LaunchedEffect(uiState.selectedProductId) {
        showFirstDialog = uiState.selectedProductId != null
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
            HeaderWithBackArrow(
                text = "Editar devolución",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.items) { product ->
                    ReturnedProductCard(
                        productName = product.product_name,
                        productDescription = product.product_description,
                        quantity = product.quantity,
                        statusId = product.status_id,
                        onClick = {
                            if (product.status_id == 9) { // En revisión
                                viewModel.selectProductForStatusChange(product.product_id)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            UpdateSalesReturnButton(
                enabled = uiState.items != uiState.updatedItems,
                onClick = {
                    viewModel.updateSalesReturn(
                        returnId = returnId,
                        onSuccess = {
                            Toast.makeText(context, "Devolución actualizada", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        },
                        onError = { message ->
                            Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            )
        }

        // Popup para cambiar estado
        if (showFirstDialog && uiState.selectedProductId != null) {
            AlertDialog(
                onDismissRequest = {
                    showFirstDialog = false
                    viewModel.selectProductForStatusChange(null)
                },
                title = { Text("Cambiar estado") },
                text = { Text("¿A qué estado deseas cambiar este producto?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.updateProductStatusLocally(uiState.selectedProductId!!, 10) // Aceptado
                        showFirstDialog = false
                        viewModel.selectProductForStatusChange(null)
                    }) {
                        Text("Aceptado")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.updateProductStatusLocally(uiState.selectedProductId!!, 11) // Dañado
                        showFirstDialog = false
                        viewModel.selectProductForStatusChange(null)
                    }) {
                        Text("Dañado")
                    }
                }
            )
        }
    }

    // Cargar datos
    LaunchedEffect(returnId) {
        viewModel.loadSalesReturn(returnId)
    }
}
