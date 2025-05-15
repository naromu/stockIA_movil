package com.example.stockia.common

import PurchaseOrder
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

@Composable
fun SelectablePurchaseOrderCard(
    order: PurchaseOrder,
    onClick: (PurchaseOrder) -> Unit
) {
    val total = order.total_amount.toDoubleOrNull() ?: 0.0
    val statusColor = Color(0xFF4CAF50) // Verde fijo para "confirmed"

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onClick(order) } // ✅ siempre clickeable
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Orden de Compra No. ${order.id}", style = MaterialTheme.typography.titleSmall, color = Color.Black)
                Spacer(Modifier.height(4.dp))
                Text("Proveedor: ${order.supplier_name ?: "Desconocido"}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text("Total: $${"%,.2f".format(total)}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text("Fecha: ${order.purchase_order_date.take(10)}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            // Indicador de estado
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(color = statusColor, shape = CircleShape)
            )
        }
    }
}
