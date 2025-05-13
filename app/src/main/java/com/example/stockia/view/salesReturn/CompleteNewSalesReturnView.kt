package com.example.stockia.view.salesReturn

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.ProductCardSelectable
import com.example.stockia.common.DynamicSelectionButton
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.routes.Routes
import com.example.stockia.viewmodel.salesReturn.CompleteNewSalesReturnViewModel

@Composable
fun CompleteNewSalesReturnView(
    navController: NavController,
    orderId: Int,
    viewModel: CompleteNewSalesReturnViewModel = viewModel()
) {
    val context = LocalContext.current

    // Cargar productos al entrar
    LaunchedEffect(orderId) {
        viewModel.loadSalesOrder(orderId)
    }

    // Mostrar errores si ocurren
    LaunchedEffect(viewModel.resultMessage) {
        viewModel.resultMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearResultMessage()
        }
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
                text = "Nueva devolución de venta",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de productos de la orden
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.products) { product ->
                    ProductCardSelectable(
                        productName = product.product_name,
                        categoryName = product.product_description, // puedes ajustar esto si tienes categorías
                        price = product.unit_price,
                        quantityAvailable = product.quantity.toString(),
                        imageUrl = null, // No tienes imageUrl en SalesOrderProduct
                        isSelected = viewModel.isProductSelected(product.product_id),
                        onClick = { viewModel.toggleProductSelection(product.product_id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón dinámico para avanzar
            DynamicSelectionButton(
                selectedCount = viewModel.selectedProducts.size,
                onClick = {
                    val selectedProductIds = viewModel.selectedProducts.joinToString(",")
                    Log.d("CompleteNewSalesReturn", "Seleccionados: $selectedProductIds")

                    // Aquí navegas al último paso del flujo (por definir)
                    navController.navigate(Routes.FinalizeSalesReturnView + "?selectedProducts=$selectedProductIds&orderId=$orderId")
                }
            )
        }
    }
}
