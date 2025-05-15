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
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.purchaseReturn.NewPurchaseReturnViewModel
import com.example.stockia.common.PurchaseOrderCard
import com.example.stockia.common.SelectablePurchaseOrderCard

@Composable
fun NewPurchaseReturnViewStep1(
    navController: NavController,
    viewModel: NewPurchaseReturnViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadPurchaseOrders()
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

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Por favor seleccione la orden de compra para su devolución",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(viewModel.purchaseOrders) { order ->
                    SelectablePurchaseOrderCard(
                        order = order,
                        onClick = {
                            navController.navigate("${Routes.CompleteNewPurchaseReturnView}/${order.id}")
                        }
                    )
                }
            }
        }
    }
}
