package com.example.stockia.view.providers

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.CustomPhoneTextField
import com.example.stockia.common.CustomTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.viewmodel.providers.NewProviderViewModel

@Composable
fun NewProviderView(
    navController: NavController,
    viewModel: NewProviderViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(viewModel.resultMessage) {
        viewModel.resultMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearResultMessage()
            if (msg == "Proveedor creado con éxito") {
                navController.navigate(Routes.ProvidersView) {
                    popUpTo(Routes.NewProviderView) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderWithBackArrow(
                text = "Crear proveedor",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Label("Nombre")
            CustomTextField(viewModel.name, { viewModel.name = it }, "Nombre")

            Spacer(modifier = Modifier.height(16.dp))

            Label("Correo Electrónico")
            CustomTextField(viewModel.email, { viewModel.email = it }, "Correo Electrónico")

            Spacer(modifier = Modifier.height(16.dp))

            Label("Número de Celular")
            CustomPhoneTextField(
                value = viewModel.phone,
                onValueChange = {viewModel.phone = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Label("Dirección")
            CustomTextField(viewModel.address, { viewModel.address = it }, "Dirección")

            Spacer(modifier = Modifier.height(32.dp))

            CustomButtonBlue(
                text = if (!viewModel.isLoading) "Crear" else "Creando...",
                enabled = viewModel.isFormValid && !viewModel.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.createProvider {
                        navController.navigate(Routes.ProvidersView) {
                            popUpTo(Routes.NewProviderView) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun Label(text: String) {
    Text(
        text = text,
        style = AppTypography.bodyLarge,
        color = Subtitulos,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Start
    )
}
