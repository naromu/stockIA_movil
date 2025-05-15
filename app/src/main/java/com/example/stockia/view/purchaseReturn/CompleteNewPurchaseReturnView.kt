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
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.ProductCardSelectable
import com.example.stockia.common.DynamicSelectionButton
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.purchaseReturn.CompleteNewPurchaseReturnViewModel

@Composable
fun CompleteNewPurchaseReturnView(
    navController: NavController,
    orderId: Int,
    viewModel: CompleteNewPurchaseReturnViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(orderId) {
        viewModel.loadPurchaseOrder(orderId)
    }

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
                text = "Nueva devolución de compra",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Seleccione los productos a devolver",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.products) { product ->
                    ProductCardSelectable(
                        productName = product.product_name ?: "Producto sin nombre",
                        categoryName = product.product_description ?: "Sin descripción",
                        price = product.unit_cost,
                        quantityAvailable = product.quantity.toString(),
                        imageUrl = null,
                        isSelected = viewModel.isProductSelected(product.product_id),
                        onClick = { viewModel.toggleProductSelection(product.product_id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DynamicSelectionButton(
                selectedCount = viewModel.selectedProducts.size,
                onClick = {
                    val selectedProductIds = viewModel.selectedProducts.joinToString(",")
                    navController.navigate("${Routes.FinalizePurchaseReturnView}?selectedProducts=$selectedProductIds&orderId=$orderId")
                }
            )
        }
    }
}
