package com.example.stockia.view.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.ProductTextButton
import com.example.stockia.common.CustomTextField
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.stock.StockViewModel


@Composable
fun StockListView(
    navController: NavController,
    viewModel: StockViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery = viewModel.searchQuery
    val filteredProducts = viewModel.filteredProducts

    // Cargar productos al ingresar
    LaunchedEffect(Unit) {
        viewModel.loadProducts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoBase)
            .padding(24.dp)
    ) {
        HeaderWithBackArrow(
            text = "Stock",
            onClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            label = "Buscar producto..."
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filteredProducts) { product ->
                ProductTextButton(
                    name = product.name,
                    imageUrl = product.imageUrl,
                    description = product.description,
                    unitPrice = product.unitPrice,
                    quantity = product.quantity,
                    onClick = {
                        val quantityInt = product.quantity.toDoubleOrNull()?.toInt() ?: 0
                        navController.navigate("EditStockView/${product.id}/$quantityInt")
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StockListViewPreview() {
    val navController = rememberNavController()
    StockListView(navController = navController)
}

