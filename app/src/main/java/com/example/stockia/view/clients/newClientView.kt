package com.example.stockia.view.clients

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
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.viewmodel.clients.NewClientViewModel

@Composable
fun NewClientView(
    navController: NavController,
    viewModel: NewClientViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(viewModel.resultMessage) {
        viewModel.resultMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearResultMessage()
            if (msg == "Cliente creado con éxito") {
                navController.navigate(Routes.ClientsView) {
                    popUpTo(Routes.NewClientView) { inclusive = true }
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
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HeaderWithBackArrow(
                text = "Crear cliente",
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
                text = if (!viewModel.isLoading) "Crear" else "Creando...",
                enabled = viewModel.isFormValid && !viewModel.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.createClient {
                        navController.navigate(Routes.ClientsView) {
                            popUpTo(Routes.NewClientView) { inclusive = true }
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
