package com.example.stockia.view.products

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.R
import com.example.stockia.common.CategoryTextButton
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.common.ProductTextButton
import com.example.stockia.routes.Routes
import com.example.stockia.scanner.PortraitCaptureActivity
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.viewmodel.products.ProductsViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.ScanOptions.ALL_CODE_TYPES

@Composable
fun ProductsView(
    navController: NavController,
    viewModel: ProductsViewModel = viewModel()
) {
    val context = LocalContext.current

    val barcodeLauncher = rememberLauncherForActivityResult(
        contract = ScanContract()
    ) { result: ScanIntentResult ->
        result.contents?.let { viewModel.onBarCodeScanned(it) }
    }

    LaunchedEffect(viewModel.resultMessage) {
        viewModel.resultMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearResultMessage()
        }
    }

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.loadProducts()
    }

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
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderWithBackArrow(
                text = "Productos",
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
                    label = "Buscar nombre o código de barras",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = painterResource(R.drawable.barcode),
                    contentDescription = "Nuevo",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            val options = ScanOptions().apply {
                                setDesiredBarcodeFormats(ALL_CODE_TYPES)
                                setPrompt("Apunta al código de barras")
                                setCaptureActivity(PortraitCaptureActivity::class.java)
                            }
                            barcodeLauncher.launch(options)
                        }
                )
                Spacer(Modifier.width(16.dp))
                Image(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = "Nuevo producto",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            navController.navigate(Routes.CreateProductView)
                        }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            SwipeRefresh(
                state = rememberSwipeRefreshState(viewModel.isLoading),
                onRefresh = { viewModel.loadProducts() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.filteredProducts) { product ->
                        ProductTextButton(
                            name = product.name,
                            description = product.description,
                            unitPrice = product.unitPrice,
                            imageUrl = product.imageUrl,
                            quantity = product.quantity,
                            onClick = {
                                navController.navigate("${Routes.EditProductView}/${product.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductsViewPreview() {
    StockIATheme {
        ProductsView(navController = rememberNavController())
    }
}