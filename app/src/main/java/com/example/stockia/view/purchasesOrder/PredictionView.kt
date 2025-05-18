package com.example.stockia.view.purchasesOrder

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.model.NavSelectedProduct
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.model.Prediction
import com.example.stockia.viewmodel.purchaseOrder.PredictionViewModel


@Composable
fun PredictionView(
    navController: NavController,
    vm: PredictionViewModel = viewModel()
) {
    val context = LocalContext.current
    val isLoading by vm.isLoading.collectAsState()
    val error by vm.error.collectAsState()
    val preds by vm.predictions.collectAsState()

    val names by vm.productsMap.collectAsState()
    // Mostrar toasts de error
    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(BlancoBase)
            .padding(24.dp)
    ) {
        Column {
            HeaderWithBackArrow(
                text = "Análisis de escasez",
                onClick = {
                    navController.navigate(Routes.PurchasesOrdersView) {
                        popUpTo(Routes.PredictionView) { inclusive = true }
                    }
                }
            )
            Spacer(Modifier.height(16.dp))

            when {
                isLoading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator( color = Color.Black)
                }
                error != null -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error al cargar predicciones ${error}", color = MaterialTheme.colorScheme.error)
                }
                else -> {
                    // Mostrar solo los con shortageDate != null
                    val atRisk = preds.filter { it.shortageDate != null }
                    if (atRisk.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No se detectó riesgo de escasez.")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(atRisk) { p ->
                                val name = names[p.productId] ?: "Producto ${p.productId}"
                                val qty = p.recommendedUnits ?: 1
                                PredictionCard(
                                    pred = p,
                                    productName = name,
                                    onClick = {
                                        val navArg = NavSelectedProduct(p.productId, qty)
                                        navController.currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("preselectedProducts", listOf(navArg))
                                        navController.navigate(Routes.CompletePurchasesOrderViewWithArgs)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun PredictionCard(
    pred: Prediction,
    productName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                productName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))
            Text(
                pred.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(Modifier.height(8.dp))

            pred.recommendedUnits?.let { units ->
                Text(
                    "Unidades recomendadas: $units",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
                Spacer(Modifier.height(8.dp))
            }

            pred.replenishmentPlan?.let {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Plan recomendado:",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black
                )
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
    }
    Spacer(Modifier.height(16.dp))

}