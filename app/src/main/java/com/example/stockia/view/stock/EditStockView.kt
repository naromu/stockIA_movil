package com.example.stockia.view.stock

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.viewmodel.stock.EditStockViewModel

@Composable
fun EditStockView(
    navController: NavController,
    productId: Int,
    currentStock: Int,
    viewModel: EditStockViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(productId) {
        viewModel.setInitialStock(productId, currentStock)
    }

    LaunchedEffect(viewModel.resultMessage) {
        when (viewModel.resultMessage) {
            "success" -> {
                Toast.makeText(context, "Stock actualizado correctamente", Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
                navController.popBackStack()
            }
            null -> Unit
            else -> {
                Toast.makeText(context, viewModel.resultMessage, Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .background(BlancoBase)
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderWithBackArrow(
                text = "Editar stock",
                onClick = { navController.popBackStack() }
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Cantidad disponible",
                style = AppTypography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = viewModel.newStock.toString(),
                onValueChange = viewModel::onStockChange,
                label = "Cantidad",
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(32.dp))

            CustomButtonBlue(
                text = if (!viewModel.isLoading) "Guardar" else "Guardando",
                enabled = viewModel.isFormValid && !viewModel.isLoading,
                onClick = { viewModel.updateStock(productId) }
            )
        }
    }
}
