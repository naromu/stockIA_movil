package com.example.stockia.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stockia.model.PurchaseReturn


@Composable
fun PurchaseReturnCard(purchaseReturn: PurchaseReturn) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Devolución No. ${purchaseReturn.id}", style = MaterialTheme.typography.titleSmall)
            Text("Orden de compra: ${purchaseReturn.purchase_order_id}")
            Text("Fecha: ${purchaseReturn.return_date.take(10)}")
            if (purchaseReturn.notes.isNotBlank()) {
                Text("Notas: ${purchaseReturn.notes}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
