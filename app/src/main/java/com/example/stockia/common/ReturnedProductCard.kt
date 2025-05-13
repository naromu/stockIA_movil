package com.example.stockia.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockia.ui.theme.AppTypography

@Composable
fun ReturnedProductCard(
    productName: String,
    productDescription: String,
    quantity: Int,
    statusId: Int,
    onClick: () -> Unit = {}
) {
    val (statusText, statusColor) = when (statusId) {
        9 -> "En revisión" to Color(0xFFFFC107) // Amarillo
        10 -> "Aceptado" to Color(0xFF4CAF50)   // Verde
        11 -> "Dañado" to Color(0xFFF44336)     // Rojo
        else -> "Desconocido" to Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(statusColor, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = productName, style = AppTypography.bodyLarge)
                Text(text = productDescription, style = AppTypography.labelSmall, color = Color.Gray)
                Text(text = "Cantidad: $quantity", style = AppTypography.bodySmall)
                Text(text = "Estado: $statusText", style = AppTypography.bodySmall)
            }
        }
    }
}
