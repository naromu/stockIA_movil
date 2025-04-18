package com.example.stockia.common


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.Gris
import com.example.stockia.ui.theme.StockIATheme

// Define el transformador de formato telefónico
class PhoneNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = phoneNumberFilter(text.text)
        return TransformedText(
            AnnotatedString(formatted),
            PhoneNumberOffsetMapper
        )
    }
}

private fun phoneNumberFilter(text: String): String {
    val digits = text.take(10).filter { it.isDigit() }
    return when {
        digits.isEmpty() -> ""
        digits.length <= 3 -> "($digits"
        digits.length <= 6 -> "(${digits.take(3)}) ${digits.drop(3)}"
        else -> "(${digits.take(3)}) ${digits.drop(3).take(3)}-${digits.drop(6)}"
    }
}

// Mapeador de posiciones para el cursor
private val PhoneNumberOffsetMapper = object : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        return when {
            offset <= 0 -> 0
            offset <= 3 -> offset + 1    // Agrega "("
            offset <= 6 -> offset + 3    // Agrega ") "
            else -> offset + 4           // Agrega "-"
        }
    }

    override fun transformedToOriginal(offset: Int): Int {
        return when {
            offset <= 1 -> 0
            offset <= 5 -> maxOf(0, offset - 2)
            offset <= 10 -> maxOf(0, offset - 4)
            else -> maxOf(0, offset - 4)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPhoneTextField(
    value: String = "",
    onValueChange: (String) -> Unit ={},
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Phone
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newText ->
            // Filtra solo dígitos (máximo 10)
            val filtered = newText.filter { it.isDigit() }.take(10)
            onValueChange(filtered)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gris,  // Borde en reposo
            focusedBorderColor = AzulPrincipal,     // Borde al hacer clic
            cursorColor = AzulPrincipal,
            containerColor = Color.White

        ),
        keyboardOptions = keyboardOptions,
        visualTransformation = PhoneNumberVisualTransformation()
    )
}

@Preview
@Composable
fun CustomPhoneTextFieldPreview(){
    StockIATheme {
        CustomPhoneTextField(value = "3102989733")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    allowedPattern: Regex = Regex("[^\\w\\sáéíóúñÁÉÍÓÚÑ@.,-]"),
    maxLength: Int = 100,
    modifier: Modifier = Modifier.fillMaxWidth()
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
        modifier = modifier,
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

@Preview
@Composable
fun CustomTextFieldPreview(){
    StockIATheme {
        CustomTextField(value ="Hola Como estás?")
    }
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
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPasswordField(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String = "",
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

@Preview
@Composable
fun CustomPasswordFieldPreview(){
    StockIATheme {
        CustomPasswordField(value = "ContrasenaSecreta", isPasswordVisible = false) {
        }
    }
}
