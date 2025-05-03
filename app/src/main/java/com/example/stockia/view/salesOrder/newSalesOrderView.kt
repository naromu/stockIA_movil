package com.example.stockia.view.salesOrder

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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.SearchWithFilterBar
import com.example.stockia.common.ProductCardSelectable
import com.example.stockia.common.DynamicSelectionButton
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.R
import com.example.stockia.viewmodel.salesOrder.NewSalesOrderViewModel


@Composable
fun NewSalesOrderView(
    navController: NavController,
    viewModel: NewSalesOrderViewModel = viewModel()


) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Cargar productos y categorías al entrar
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
        viewModel.loadCategories()
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
                text = "Crear orden de venta",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buscador con filtro
            SearchWithFilterBar(
                searchText = viewModel.searchText,
                onSearchChange = viewModel::onSearchTextChange,
                onFilterClick = viewModel::toggleFilterDialog
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lista de productos
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.filteredProducts) { product ->
                    ProductCardSelectable(
                        productName = product.name,
                        categoryName = viewModel.getCategoryName(product.categoryId),
                        price = product.unitPrice,
                        quantityAvailable = product.quantity,
                        imageUrl = product.imageUrl,
                        isSelected = viewModel.isProductSelected(product.id),
                        onClick = { viewModel.toggleProductSelection(product.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón dinámico
            DynamicSelectionButton(
                selectedCount = viewModel.selectedProducts.size,
                onClick = {
                    // Convertir los productos seleccionados a una cadena separada por comas
                    val selectedProductIds = viewModel.selectedProducts.joinToString(",") { it.toString() }

                    Log.d("NewSalesOrderView", "Productos seleccionados: $selectedProductIds")

                    // Pasamos los IDs de los productos seleccionados como un argumento de navegación
                    navController.navigate(Routes.CompleteSalesOrderView + "?selectedProducts=$selectedProductIds")
                }
            )


        }
    }

    // Diálogo de filtro por categoría (opcional, si lo necesitas luego)
    if (viewModel.showFilterDialog) {
        // Aquí iría el filtro por categoría
    }
}
