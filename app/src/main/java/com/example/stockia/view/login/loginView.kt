package com.example.stockia.view.login

import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme

import com.example.stockia.viewmodel.login.LoginViewModel

@Composable
fun LoginView(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val email = loginViewModel.email
    val password = loginViewModel.password
    val isPasswordVisible = loginViewModel.isPasswordVisible

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current

    LaunchedEffect(loginViewModel.loginResult) {
        when (val result = loginViewModel.loginResult) {
            "success" -> {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                loginViewModel.clearLoginResult()
                navController?.navigate(Routes.HomeView) {
                    popUpTo(Routes.LoginView) { inclusive = true }
                }
            }

            "Cuenta pendiente por confirmar. Se ha enviado un nuevo código de confirmación a tu correo" -> {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                loginViewModel.clearLoginResult()
                navController?.navigate(Routes.ConfirmEmailView)
            }

            null -> Unit

            else -> {
                Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                loginViewModel.clearLoginResult()
            }
        }
    }

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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Bienvenido a",
                style = AppTypography.bodyLarge
            )

            Text(
                text = "stockIA",
                style = AppTypography.titleLarge,
                color = AzulPrincipal
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Correo",
                style = AppTypography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomTextField(
                value = email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = "Correo"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contraseña",
                style = AppTypography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomPasswordField(
                value = password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = "Contraseña",
                isPasswordVisible = isPasswordVisible,
                onVisibilityToggle = { loginViewModel.togglePasswordVisibility() }
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomButtonBlue(
                text = if (loginViewModel.isLoading) "Iniciando..." else "Iniciar sesión",
                onClick = { loginViewModel.onLoginClick() },
                enabled = !loginViewModel.isLoading
            )



            Spacer(modifier = Modifier.height(14.dp))


            OutlinedButton(
                onClick = { navController?.navigate(Routes.RegisterView)  },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(60.dp)
                    .background(Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, AzulPrincipal)
            ) {
                Text("Registrarse", color = AzulPrincipal, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            TextButton(onClick = {navController?.navigate(Routes.ResetPasswordOneView)  }) {
                Text("Restablecer contraseña", color = AzulPrincipal, fontSize = 16.sp,)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    StockIATheme {
        LoginView(
            navController = rememberNavController()
        )
    }
}