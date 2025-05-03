package com.example.stockia.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.stockia.ui.theme.AppTypography
import com.example.stockia.ui.theme.AzulPrincipal
import com.example.stockia.ui.theme.Gris
import androidx.compose.ui.Alignment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterableDropdown(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Mostrar el título o label encima del Dropdown
        Text(
            text = label,  // Aquí se coloca el título
            style = AppTypography.bodyLarge,  // Asegúrate de usar el estilo adecuado para el título
            modifier = Modifier.padding(bottom = 8.dp)  // Espacio entre el label y el dropdown
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            // El OutlinedTextField para mostrar la búsqueda o el texto seleccionado
            OutlinedTextField(
                value = selectedOption ?: searchQuery.text,  // Mostrar el texto seleccionado o la búsqueda
                onValueChange = {
                    searchQuery = TextFieldValue(it)
                    expanded = true
                },
                readOnly = false,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor() // Asegura que el menú se ancle correctamente al campo de entrada
                    .fillMaxWidth(), // Aseguramos que el TextField ocupe todo el ancho disponible
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Gris,
                    focusedBorderColor = AzulPrincipal,
                    cursorColor = AzulPrincipal,
                    containerColor = Color.White
                )
            )

            // El DropdownMenu para mostrar las opciones
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth() // Hacer que el DropdownMenu ocupe el mismo ancho
                    .background(Color.White)
            ) {
                val filteredOptions = options.filter {
                    it.contains(searchQuery.text, ignoreCase = true)
                }

                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, style = AppTypography.bodyMedium) },
                        onClick = {
                            onOptionSelected(option)
                            searchQuery = TextFieldValue("")
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FilterableDropdownPreview() {
    var selected by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        FilterableDropdown(
            label = "Proveedor", // Aquí se pasa el título
            options = listOf("Proveedor ABC", "Proveedor XYZ", "Proveedor 123"),
            selectedOption = selected,
            onOptionSelected = { selected = it }
        )
    }
}
