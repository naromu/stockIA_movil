package com.example.stockia.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockia.common.CustomTextField
import com.example.stockia.ui.theme.AzulPrincipal
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.*

@Composable
fun SearchWithFilterBar(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomTextField(
            value = searchText,
            onValueChange = onSearchChange,
            label = "Nombre del producto",
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = onFilterClick) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filtrar",
                tint = AzulPrincipal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchWithFilterBarPreview() {
    val searchQuery = remember { mutableStateOf("") }

    Surface(color = MaterialTheme.colorScheme.background) {
        SearchWithFilterBar(
            searchText = searchQuery.value,
            onSearchChange = { searchQuery.value = it },
            onFilterClick = { /* Acción de prueba para preview */ }
        )
    }
}


