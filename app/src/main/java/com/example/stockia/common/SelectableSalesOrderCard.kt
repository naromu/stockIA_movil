package com.example.stockia.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockia.model.SalesOrder

@Composable
fun SelectableSalesOrderCard(
    order: SalesOrder,
    onClick: () -> Unit
) {
    val total = order.total_amount?.toDoubleOrNull() ?: 0.0

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 4.dp)
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
                    text = "Orden de Venta No. ${order.id}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black
                )
                Text(text = "Cliente: ${order.customer_name ?: "Desconocido"}")
                Text(text = "$${"%,.0f".format(total)}")
            }

            // Círculo verde (status_id == 2 = Confirmado)
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color = Color(0xFF4CAF50), shape = CircleShape)
            )
        }
    }
}
