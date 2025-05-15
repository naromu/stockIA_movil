package com.example.stockia.view.purchaseReturn

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.R
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.SearchWithFilterBar
import com.example.stockia.model.PurchaseReturn
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.viewmodel.purchaseReturn.PurchaseReturnsViewModel
import com.example.stockia.common.PurchaseReturnCard

@Composable
fun PurchaseReturnsView(
    navController: NavController,
    viewModel: PurchaseReturnsViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadReturns()
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
                text = "Devoluciones de compra",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = viewModel.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    label = "Buscar devolución",
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Nuevo",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigate(Routes.NewPurchaseReturnStep1)
                        }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(viewModel.filteredReturns) { ret ->
                    PurchaseReturnCard(purchaseReturn = ret)
                }
            }
        }
    }
}
