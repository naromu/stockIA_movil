package com.example.stockia.view.login


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stockia.R
import com.example.stockia.common.CustomPasswordField
import com.example.stockia.common.CustomTextField
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.Subtitulos

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockia.Greeting
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.CustomPhoneTextField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme

import com.example.stockia.viewmodel.LoginViewModel
import com.example.stockia.viewmodel.RegisterViewModel

@Composable
fun RegisterView(
    registerViewModel: RegisterViewModel = viewModel()
) {

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
            .background(BlancoBase)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            HeaderWithBackArrow(text = "Crear cuenta")

            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = "Nombre propietario",
                style = MaterialTheme.typography.labelSmall,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = registerViewModel.ownerName,
                onValueChange = { registerViewModel.onOwnerNameChange(it) },
                label = "Correo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nombre empresa",
                style = MaterialTheme.typography.labelSmall,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = registerViewModel.companyName,
                onValueChange = { registerViewModel.onCompanyNameChange(it) },
                label = "Correo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Correo Electronico",
                style = MaterialTheme.typography.labelSmall,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = registerViewModel.email,
                onValueChange = { registerViewModel.onEmailChange(it) },
                label = "Correo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Número de Celular",
                style = MaterialTheme.typography.labelSmall,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomPhoneTextField(
                value = registerViewModel.phoneNumber,
                onValueChange = { registerViewModel.onPhoneNumberChange(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contraseña",
                style = MaterialTheme.typography.labelSmall,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomPasswordField(
                value = registerViewModel.password,
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = "Contraseña",
                isPasswordVisible = registerViewModel.isPasswordVisible,
                onVisibilityToggle = { registerViewModel.togglePasswordVisibility() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Confirmar contraseña",
                style = MaterialTheme.typography.labelSmall,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomPasswordField(
                value = registerViewModel.confirmPassword,
                onValueChange = { registerViewModel.onPasswordChange(it) },
                label = "Contraseña",
                isPasswordVisible = registerViewModel.isPasswordVisible,
                onVisibilityToggle = { registerViewModel.togglePasswordVisibility() }
            )


            Spacer(modifier = Modifier.height(24.dp))

            CustomButtonBlue(
                text = "Continuar",
                onClick = { registerViewModel.onRegisterClick() }
            )

            Spacer(modifier = Modifier.height(14.dp))

        }
    }
}


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun RegisterPreview() {
    StockIATheme {
        RegisterView()
    }
}
