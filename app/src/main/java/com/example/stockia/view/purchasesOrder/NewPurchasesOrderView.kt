package com.example.stockia.view.purchasesOrder

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.SearchWithFilterBar
import com.example.stockia.common.ProductCardSelectable
import com.example.stockia.common.DynamicSelectionButton
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.purchaseOrder.NewPurchasesOrderViewModel

@Composable
fun NewPurchasesOrderView(
    navController: NavController,
    viewModel: NewPurchasesOrderViewModel = viewModel()
) {
    val context = LocalContext.current

    // Cargar productos y categorías al iniciar
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
        viewModel.loadCategories()
    }

    // Mostrar mensajes de resultado
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
                text = "Crear orden de compra",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SearchWithFilterBar(
                searchText = viewModel.searchText,
                onSearchChange = viewModel::onSearchTextChange,
                onFilterClick = {} // Sin filtro de proveedor
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.filteredProducts) { product ->
                    ProductCardSelectable(
                        productName = product.name,
                        categoryName = viewModel.getCategoryName(product.categoryId),
                        price = product.unitCost,
                        quantityAvailable = product.quantity,
                        imageUrl = product.imageUrl,
                        isSelected = viewModel.isProductSelected(product.id),
                        onClick = { viewModel.toggleProductSelection(product.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            DynamicSelectionButton(
                selectedCount = viewModel.selectedProducts.size,
                onClick = {
                    if (viewModel.selectedProducts.isEmpty()) {
                        Toast.makeText(context, "Selecciona al menos un producto", Toast.LENGTH_SHORT).show()
                    } else {
                        // 1) Guardas la lista
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedProducts", viewModel.selectedProducts.toList())
                        // 2) Navegas sin query-params
                        navController.navigate(Routes.CompletePurchasesOrderViewWithArgs)
                    }
                }
            )

        }
    }
}
