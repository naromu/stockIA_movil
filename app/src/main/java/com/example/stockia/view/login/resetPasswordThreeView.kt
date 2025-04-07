package com.example.stockia.view.login

import android.widget.Toast
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stockia.common.CustomTextField
import com.example.stockia.ui.theme.Subtitulos

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stockia.common.CommonError
import com.example.stockia.common.CustomButtonBlue
import com.example.stockia.common.CustomPasswordField
import com.example.stockia.common.HeaderWithBackArrow
import com.example.stockia.routes.Routes
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.viewmodel.login.ConfirmEmailViewModel
import com.example.stockia.viewmodel.login.ResetPasswordThreeViewModel
import com.example.stockia.viewmodel.login.ResetPasswordTwoViewModel

@Composable
fun ResetPasswordThreeView(
    navController: NavController,
    code: String,
    resetPasswordThreeViewModel: ResetPasswordThreeViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        resetPasswordThreeViewModel.code = code
    }

    LaunchedEffect(resetPasswordThreeViewModel.resultMessage) {
        when (resetPasswordThreeViewModel.resultMessage) {
            "success" -> {
                Toast.makeText(context, "Contraseña cambiada exitosamente", Toast.LENGTH_LONG).show()
                resetPasswordThreeViewModel.clearResultMessage()
                navController.navigate(Routes.LoginView) {
                    popUpTo(Routes.ConfirmEmailView) { inclusive = true }
                }
            }

            null -> Unit

            else -> {
                Toast.makeText(context, resetPasswordThreeViewModel.resultMessage, Toast.LENGTH_LONG).show()
                resetPasswordThreeViewModel.clearResultMessage()
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
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
            ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            HeaderWithBackArrow(
                text = "Nueva contraseña",
                onClick = { navController?.popBackStack() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Crea tu nueva contraseña",
                style = MaterialTheme.typography.bodyLarge,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Contraseña",
                style = MaterialTheme.typography.labelSmall,
                color = Subtitulos,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))

            CustomPasswordField(
                value = resetPasswordThreeViewModel.password,
                onValueChange = { resetPasswordThreeViewModel.onPasswordChange(it) },
                label = "Contraseña",
                isPasswordVisible = resetPasswordThreeViewModel.isPasswordVisible,
                onVisibilityToggle = { resetPasswordThreeViewModel.togglePasswordVisibility() }
            )
            resetPasswordThreeViewModel.passwordErrors.forEach {
                CommonError(text = it)
            }

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
                value = resetPasswordThreeViewModel.confirmPassword,
                onValueChange = { resetPasswordThreeViewModel.onConfirmPasswordChange(it) },
                label = "Contraseña",
                isPasswordVisible = resetPasswordThreeViewModel.isConfirmPasswordVisible,
                onVisibilityToggle = { resetPasswordThreeViewModel.toggleConfirmPasswordVisibility() }
            )
            resetPasswordThreeViewModel.confirmPasswordError?.let {
                CommonError(text = it)
            }


            Spacer(modifier = Modifier.height(24.dp))

            CustomButtonBlue(
                text = if (resetPasswordThreeViewModel.isLoading) "Restableciendo..." else "Restablecer",
                enabled = resetPasswordThreeViewModel.isFormValid && !resetPasswordThreeViewModel.isLoading,
                onClick = { resetPasswordThreeViewModel.onRestablecerClick() }
            )
            Spacer(modifier = Modifier.height(14.dp))

        }
    }
}


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun ResetPasswordThreePreview() {
    StockIATheme {
        ResetPasswordThreeView(
            navController = rememberNavController(),
            code = "HJHJ3",

        )
    }
}
