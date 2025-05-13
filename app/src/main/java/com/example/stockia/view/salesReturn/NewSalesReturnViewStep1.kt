package com.example.stockia.view.salesReturn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.SelectableSalesOrderCard
import com.example.stockia.routes.Routes
import com.example.stockia.viewmodel.salesReturn.NewSalesReturnViewModel
import com.example.stockia.ui.theme.BlancoBase

@Composable
fun NewSalesReturnViewStep1(
    navController: NavController,
    viewModel: NewSalesReturnViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                text = "Nueva Devolución de Venta",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Por favor seleccione la orden de venta para su devolución",
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.confirmedSalesOrders) { order ->
                    // Este card lo implementaremos después
                    SelectableSalesOrderCard(
                        order = order,
                        onClick = {
                            navController.navigate("${Routes.CompleteNewSalesReturnView}/${order.id}")
                        }
                    )
                }
            }
        }
    }
}
