package com.example.stockia.common
import PurchaseOrder
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockia.model.SalesOrder
@Composable
fun PurchaseOrderCard(
    order: PurchaseOrder,
    onConfirmClick: (PurchaseOrder) -> Unit = {}
) {
    val total = order.total_amount.toDoubleOrNull() ?: 0.0
    val status = order.status_name?.lowercase()

    val statusColor = when (status) {
        "confirmed" -> Color(0xFF4CAF50)
        "pending"   -> Color(0xFFFFA000)
        else        -> Color.Gray
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
                Text("Orden de Compra No. ${order.id}", style = MaterialTheme.typography.titleSmall, color = Color.Black)
                Spacer(Modifier.height(4.dp))
                Text("Proveedor: ${order.supplier_name ?: "Desconocido"}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text("Total: $${"%,.2f".format(total)}", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(4.dp))
                Text("Fecha: ${order.purchase_order_date.take(10)}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            // Círculo de estado (con flecha si es pending)
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = statusColor, shape = CircleShape)
                )
                if (status == "pending") {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Pendiente",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}