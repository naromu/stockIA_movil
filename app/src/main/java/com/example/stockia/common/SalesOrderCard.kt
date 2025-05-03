package com.example.stockia.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockia.model.SalesOrder

@Composable
fun SalesOrderCard(
    order: SalesOrder,
    onConfirmClick: (SalesOrder) -> Unit = {}
) {
    val total = order.total_amount?.toDoubleOrNull() ?: 0.0
    val status = order.status_name?.lowercase()

    val statusLabel = when (status) {
        "confirmed" -> "Confirmada"
        "pending" -> "Pendiente"
        else -> status ?: "Desconocido"
    }

    val statusColor = when (status) {
        "confirmed" -> Color(0xFF4CAF50) // Verde
        "pending" -> Color(0xFFFFA000)   // Amarillo
        else -> Color.Gray
    }

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable(enabled = status == "pending") {
                onConfirmClick(order)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Orden De Venta No. ${order.id}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black
                )
                Text(text = "Cliente: ${order.customer_name ?: "Desconocido"}")
                Text(text = "$${"%,.0f".format(total)}") // Formato de miles con punto
            }

            // Círculo de estado
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color = statusColor, shape = CircleShape)
            )
        }
    }
}

