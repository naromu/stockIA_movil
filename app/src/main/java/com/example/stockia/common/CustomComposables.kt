package com.example.stockia.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.stockia.R
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.BlancoBase
import com.example.stockia.ui.theme.Gris
import com.example.stockia.ui.theme.Poppins
import com.example.stockia.ui.theme.StockIATheme
import com.example.stockia.ui.theme.Subtitulos
import com.example.stockia.view.login.LoginView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    allowedPattern: Regex = Regex("[^\\w\\sáéíóúñÁÉÍÓÚÑ@.,-]"),
    maxLength: Int = 100
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength) { val filteredValue = newValue.replace(allowedPattern, "")
            if (newValue == filteredValue) {
                onValueChange(newValue)
            } else {
                onValueChange(filteredValue)
            }
        }},
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gris,  // Borde en reposo
            focusedBorderColor = AzulPrincipal,   // Borde al hacer clic
            cursorColor = AzulPrincipal,
            containerColor = Color.White
        )
    )
}

@Composable
fun CommonError(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        color = Color.Red,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier.fillMaxWidth().padding(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPasswordVisible: Boolean,
    onVisibilityToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,

        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gris,  // Borde en reposo
            focusedBorderColor = AzulPrincipal,     // Borde al hacer clic
            cursorColor = AzulPrincipal,
            containerColor = Color.White

        ),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val icon = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
            IconButton(onClick = onVisibilityToggle) {
                Icon(imageVector = icon, contentDescription = null)
            }
        }
    )
}


@Composable
fun HeaderWithBackArrow(
    text: String,
    onClick: (() -> Unit)? = null, // ahora es opcional
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.back_arrow),
            contentDescription = "Back arrow",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    onClick?.invoke() ?: navController.popBackStack()
                }
        )

        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}


@Preview
@Composable
fun HeaderWithBackArrowPreview(){
    StockIATheme {
        HeaderWithBackArrow(text = "Crear cuenta")
    }
}

@Composable
fun CustomButtonBlue(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (enabled) AzulPrincipal else Color.Gray
        )
    ) {
        Text(
            text = text,
            fontFamily = Poppins,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun CustomButtonBluePreview() {
    StockIATheme {
        CustomButtonBlue(
            text = "Iniciar sesión",
            enabled = false,
            onClick = { }
        )
    }
}

