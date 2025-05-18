package com.example.stockia.view.clients

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.stockia.common.ErrorMessageBox
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.viewmodel.clients.EditClientViewModel
import com.example.stockia.routes.Routes

@Composable
fun EditClientView(
    navController: NavController,
    clientId: Int,
    viewModel: EditClientViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    // cargar cliente al entrar a la pantalla
    LaunchedEffect(clientId) {
        viewModel.loadClient(clientId)
    }

    // mostrar mensajes según resultado
    LaunchedEffect(viewModel.resultMessage) {
        when (val msg = viewModel.resultMessage) {
            "Cliente actualizado" -> {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
                navController.popBackStack()
            }

            "Cliente eliminado" -> {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                viewModel.clearResultMessage()
                navController.popBackStack()
            }

            null -> Unit

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
                text = "Editar cliente",
                onClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Campo: Nombre ---
            Label("Nombre")
            CustomTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = "Nombre",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo: Correo ---
            Label("Correo Electrónico")
            CustomTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = "Correo Electrónico",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo: Teléfono ---
            Label("Número de Celular")
            CustomPhoneTextField(
                value = viewModel.phone,
                onValueChange = {viewModel.phone = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo: Dirección ---
            Label("Dirección")
            CustomTextField(
                value = viewModel.address,
                onValueChange = { viewModel.address = it },
                label = "Dirección",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            viewModel.resultMessage?.takeIf {
                it != "Cliente actualizado" && it != "Cliente eliminado"
            }?.let { message ->
                ErrorMessageBox(
                    message = message,
                    onClose = { viewModel.clearResultMessage() }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            // --- Botón: Actualizar ---
            CustomButtonBlue(
                text = if (!viewModel.isLoading) "Actualizar" else "Actualizando...",
                enabled = viewModel.isFormValid && !viewModel.isLoading,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.updateClient {
                        navController.navigate(Routes.ClientsView) {
                            navController.popBackStack()
                            navController.navigate(Routes.ClientsView)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Botón: Eliminar ---
            TextButton(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.deleteClient {
                        navController.navigate(Routes.ClientsView) {
                            navController.popBackStack()
                            navController.navigate(Routes.ClientsView)
                        }
                    }
                }
            ) {
                Text("Eliminar Cliente", color = AzulPrincipal, fontSize = 16.sp)
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
