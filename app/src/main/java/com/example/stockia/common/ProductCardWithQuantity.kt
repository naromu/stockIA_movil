package com.example.stockia.common


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.stockia.ui.theme.*

@Composable
fun ProductCardWithQuantity(
    name: String,
    description: String?,
    imageUrl: String?,
    unitPrice: Double,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl?.let { "http://173.212.224.226:3000$it" },
                contentDescription = "Producto",
                placeholder = rememberVectorPainter(Icons.Default.Add),
                error = rememberVectorPainter(Icons.Default.Add),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Gris.copy(alpha = 0.2f))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = AppTypography.bodyLarge,
                    maxLines = 2
                )
                if (!description.isNullOrEmpty()) {
                    Text(
                        text = description,
                        style = AppTypography.labelSmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Precio: $${"%.2f".format(unitPrice)}",
                    style = AppTypography.bodyMedium
                )
                Text(
                    text = "Total: $${"%.2f".format(unitPrice * quantity)}",
                    style = AppTypography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDecrease, enabled = quantity > 1) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Disminuir"
                    )
                }
                Text(
                    text = quantity.toString(),
                    style = AppTypography.bodyMedium,
                    modifier = Modifier.width(24.dp),
                )
                IconButton(onClick = onIncrease) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Aumentar"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardWithQuantityPreview() {
        ProductCardWithQuantity(
            name = "Café Especial Supremo",
            description = "Granos seleccionados 100% arábica",
            imageUrl = null,
            unitPrice = 15000.0,
            quantity = 2,
            onIncrease = {},
            onDecrease = {}
        )
}
