package com.example.stockia.view.providers

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.viewmodel.providers.EditProviderViewModel

@Composable
fun EditProviderView(
    navController: NavController,
    providerId: Int,
    viewModel: EditProviderViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // Cargar proveedor al entrar
    LaunchedEffect(providerId) {
        viewModel.loadProvider(providerId)
    }

    // Mostrar mensaje de éxito o error
    LaunchedEffect(viewModel.resultMessage) {
        when (val msg = viewModel.resultMessage) {
            "Proveedor actualizado" -> {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
                navController.navigate(Routes.ProvidersView) {
                    popUpTo(Routes.ProvidersView) { inclusive = true }
                    launchSingleTop = true
                }
            }

            "Proveedor eliminado" -> {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
                navController.navigate(Routes.ProvidersView) {
                    popUpTo(Routes.ProvidersView) { inclusive = true }
                    launchSingleTop = true
                }
            }

            null -> Unit
            else -> {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
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
                text = "Editar proveedor",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Label("Nombre")
            CustomTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = "Nombre",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Label("Correo Electrónico")
            CustomTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = "Correo Electrónico",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Label("Número de Celular")
            CustomPhoneTextField(
                value = viewModel.phone,
                onValueChange = {viewModel.phone = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Label("Dirección")
            CustomTextField(
                value = viewModel.address,
                onValueChange = { viewModel.address = it },
                label = "Dirección",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            CustomButtonBlue(
                text = if (!viewModel.isLoading) "Actualizar" else "Actualizando...",
                enabled = viewModel.isFormValid && !viewModel.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.updateProvider(providerId) {
                        navController.navigate(Routes.ProvidersView) {
                            popUpTo(Routes.ProvidersView) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.deleteProvider(providerId) {
                        navController.navigate(Routes.ProvidersView) {
                            popUpTo(Routes.ProvidersView) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            ) {
                Text("Eliminar Proveedor", color = AzulPrincipal, fontSize = 16.sp)
            }
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
