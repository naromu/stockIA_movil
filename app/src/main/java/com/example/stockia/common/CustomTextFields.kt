package com.example.stockia.common


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.Gris
import com.example.stockia.ui.theme.StockIATheme

import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.VisualTransformation

class DashedPhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(10)
        val transformed = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 2 || i == 5) append('-')
            }
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 6 -> offset + 1
                    offset <= 10 -> offset + 2
                    else -> 12
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when {
                    offset <= 3 -> offset
                    offset <= 7 -> offset - 1
                    offset <= 12 -> offset - 2
                    else -> 10
                }
            }
        }
        return TransformedText(AnnotatedString(transformed), offsetMapping)
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
        visualTransformation = DashedPhoneVisualTransformation()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    label: String,
    options: List<Pair<Int, String>>,
    selected: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = options.firstOrNull { it.first == selected }?.second.orEmpty(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Gris,
                    focusedBorderColor = AzulPrincipal,
                    cursorColor = AzulPrincipal,
                    containerColor = Color.White
                ),
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onSelect(id)
                            expanded = false
                        }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomNumericTextField(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    label: String = "",
    maxLength: Int = 15,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isInteger: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            val filteredValue = when {
                newValue.isEmpty() -> ""
                isInteger -> newValue.filter { it.isDigit() }
                else -> newValue.filter { it.isDigit() || it == '.' }
            }.take(maxLength)

            if (filteredValue != value) {
                onValueChange(filteredValue)
            }
        },
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isInteger) KeyboardType.Number else KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Gris,
            focusedBorderColor = AzulPrincipal,
            cursorColor = AzulPrincipal,
            containerColor = Color.White
        )
    )
}


@Preview
@Composable
fun CustomNumericTextFieldPreview(){
    StockIATheme {
        CustomNumericTextField()    }
}