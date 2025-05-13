package com.example.stockia.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockia.ui.theme.AppTypography

@Composable
fun ProductReturnCard(
    productName: String,
    productDescription: String,
    quantity: Int,
    maxQuantity: Int,
    statusId: Int,
    onQuantityChange: (Int) -> Unit,
    onStatusChange: (Int) -> Unit
) {
    val estados = listOf(
        9 to "En revisión",
        10 to "Aceptado",
        11 to "Dañado"
    )

    val estadoColor = when (statusId) {
        9 -> Color(0xFFFFA000) // Amarillo
        10 -> Color(0xFF4CAF50) // Verde
        11 -> Color(0xFFF44336) // Rojo
        else -> Color.Gray
    }

    val selectedLabel = estados.find { it.first == statusId }?.second ?: "Estado"

    var dropdownExpanded by remember { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = productName, style = AppTypography.bodyLarge)
            Text(text = productDescription, style = AppTypography.bodySmall, color = Color.Gray)

            Spacer(modifier = Modifier.height(12.dp))

            // Selector de cantidad
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Cantidad:", style = AppTypography.bodyMedium)
                Spacer(Modifier.width(8.dp))
                Button(onClick = { if (quantity > 1) onQuantityChange(quantity - 1) }) {
                    Text("-")
                }
                Spacer(Modifier.width(12.dp))
                Text(quantity.toString())
                Spacer(Modifier.width(12.dp))
                Button(onClick = { if (quantity < maxQuantity) onQuantityChange(quantity + 1) }) {
                    Text("+")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Dropdown de estado
            Box {
                Button(
                    onClick = { dropdownExpanded = true },
                    colors = ButtonDefaults.buttonColors(containerColor = estadoColor)
                ) {
                    Text(selectedLabel, color = Color.White)
                }

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    estados.forEach { (id, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                onStatusChange(id)
                                dropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
