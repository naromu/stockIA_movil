package com.example.stockia.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stockia.ui.theme.AppTypography

@Composable
fun QuantitySelectorCard(
    productName: String,
    productDescription: String,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = productName,
                style = AppTypography.bodyLarge
            )
            Text(
                text = productDescription,
                style = AppTypography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Cantidad:", style = AppTypography.bodyMedium)
                Spacer(Modifier.width(8.dp))

                Button(
                    onClick = { if (quantity > 1) onDecrease() },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("-")
                }

                Spacer(Modifier.width(12.dp))

                Text(quantity.toString(), style = AppTypography.bodyMedium)

                Spacer(Modifier.width(12.dp))

                Button(
                    onClick = onIncrease,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("+")
                }
            }
        }
    }
}
